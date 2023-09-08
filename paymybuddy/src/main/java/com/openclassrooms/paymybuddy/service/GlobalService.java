package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.entity.Commission;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.exception.UserNotFoundException;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(propagation = Propagation.MANDATORY, timeout = 60)
public class GlobalService {
    @Autowired
    CurrencyService currencyService;
    @Autowired
    CommissionService commissionService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;

    @Autowired
    TransactionRepository transactionRepository;

    public InternalTransactionDto addInternalTransaction(InternalTransactionDto internalTransactionDto) {
        if (internalTransactionDto == null || internalTransactionDto.isEmpty())
            throw new IllegalArgumentException("Invalid transaction");
        UserDto sender = userService.findById(internalTransactionDto.getSenderId());
        if (sender == null || sender.isEmpty())
            throw new UserNotFoundException("Sender not found");
        UserDto receiver = userService.findById(internalTransactionDto.getReceiverId());
        if (receiver == null || sender.isEmpty())
            throw new UserNotFoundException("Receiver not found");
        internalTransactionDto.setCommissionAmount(calculateCommissionAmount(internalTransactionDto));
        Double fullTransactionAmount = internalTransactionDto.getAmount() + internalTransactionDto.getCommissionAmount();
        if (sender.getAccountBalance() < (fullTransactionAmount)) {
            throw new IllegalArgumentException("Not enough money on user account");
        }
        internalTransactionDto.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
        Transaction transaction = new Transaction(internalTransactionDto);
        userService.removeFromAccountBalance(sender, fullTransactionAmount);
        userService.addToAccountBalance(receiver, transaction.getAmount());
        return new InternalTransactionDto(transactionRepository.save(transaction));
    }

    public ExternalTransactionDto addExternalTransaction(ExternalTransactionDto externalTransactionDto) {
        if(externalTransactionDto == null
                || externalTransactionDto.isEmpty())
            throw new IllegalArgumentException("Invalid transaction");
        UserDto sender = userService.findById(externalTransactionDto.getSenderId());
        if (sender == null || sender.isEmpty())
            throw new UserNotFoundException("Sender not found");
        externalTransactionDto.setCommissionAmount(calculateCommissionAmount(externalTransactionDto));
        Double fullTransactionAmount = externalTransactionDto.getAmount() + externalTransactionDto.getCommissionAmount();
        externalTransactionDto.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
        Transaction transaction = new Transaction(externalTransactionDto);
        if(externalTransactionDto.isToIban()) {
            if (sender.getAccountBalance() < (fullTransactionAmount)) {
                throw new IllegalArgumentException("Not enough money on user account");
            } else {
                userService.removeFromAccountBalance(sender, fullTransactionAmount);
                return new ExternalTransactionDto(transactionRepository.save(transaction));
            }
        }
        else {
            userService.addToAccountBalance(sender, transaction.getAmount());
            return new ExternalTransactionDto(transactionRepository.save(transaction));
        }
    }

    public List<PastTransactionDto> getPastTransactions(UserDto userDto) {
        if(userDto==null || userDto.isEmpty())
            throw new IllegalArgumentException("Invalid user");
        List<PastTransactionDto> result = new ArrayList<>();
        List<TransactionDto> transactions;
        transactions = transactionService.findBySenderId(userDto.getId());
        if (transactions != null && !transactions.isEmpty()) {
            for (TransactionDto transactionDto : transactions) {
                if (transactionDto != null && !transactionDto.isEmpty()) {
                    if (transactionDto.isInternalTransaction()) {
                        result.add(new PastTransactionDto(transactionDto.getId(),
                                userService.findById(((InternalTransactionDto) transactionDto).getReceiverId()).getUsername(),
                                transactionDto.getDescription(),
                                -transactionDto.getAmount(),
                                currencyService.findById(transactionDto.getCurrencyId()).getName()));
                    } else if (transactionDto.isExternalTransaction()) {
                        if (((ExternalTransactionDto) transactionDto).isToIban()) {
                            result.add(new PastTransactionDto(transactionDto.getId(),
                                    userService.findById(transactionDto.getSenderId()).getUsername(),
                                    transactionDto.getDescription(),
                                    -transactionDto.getAmount(),
                                    currencyService.findById(transactionDto.getCurrencyId()).getName()));
                        } else {
                            result.add(new PastTransactionDto(transactionDto.getId(),
                                    userService.findById(transactionDto.getSenderId()).getUsername(),
                                    transactionDto.getDescription(),
                                    transactionDto.getAmount(),
                                    currencyService.findById(transactionDto.getCurrencyId()).getName()));
                        }
                    }
                }
            }
        }
        transactions = transactionService.findByReceiverId(userDto.getId());
        if (transactions != null && !transactions.isEmpty()) {
            for (TransactionDto transactionDto : transactions) {
                if (transactionDto != null && !transactionDto.isEmpty() && transactionDto.isInternalTransaction()) {
                    result.add(new PastTransactionDto(transactionDto.getId(),
                            userService.findById(transactionDto.getSenderId()).getUsername(),
                            transactionDto.getDescription(),
                            transactionDto.getAmount(),
                            currencyService.findById(transactionDto.getCurrencyId()).getName()));
                }
            }
        }
        result.sort(Comparator.comparing(PastTransactionDto::getId).reversed());
        return result;
    }

    public Double calculateCommissionAmount(TransactionDto transactionDto) {
        if(transactionDto == null
                || transactionDto.getCommissionId() == null
                || transactionDto.getAmount() == null)
            throw new IllegalArgumentException("Invalid transaction");
        Commission commission = commissionService.findById(transactionDto.getCommissionId());
        if(commission == null)
            throw new IllegalArgumentException("Invalid commission");
        return transactionDto.getAmount() * commission.getRate();
    }
}
