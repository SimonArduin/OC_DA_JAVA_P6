package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.entity.Commission;
import com.openclassrooms.paymybuddy.exception.UserNotFoundException;
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
@Transactional(propagation = Propagation.REQUIRED, timeout = 60)
public class GlobalService {

    @Autowired
    CurrencyService currencyService;

    @Autowired
    CommissionService commissionService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    public InternalTransactionDto addInternalTransaction(InternalTransactionDto internalTransactionDto) {

        if (internalTransactionDto == null || internalTransactionDto.isEmpty())
            throw new IllegalArgumentException("Invalid transaction");

        // get sender from database
        UserDto sender = userService.findById(internalTransactionDto.getSenderId());

        if (sender == null || sender.isEmpty())
            throw new UserNotFoundException("Sender not found");

        // get receiver from database
        UserDto receiver = userService.findById(internalTransactionDto.getReceiverId());

        if (receiver == null || sender.isEmpty())
            throw new UserNotFoundException("Receiver not found");

        // calculate full amount of transaction
        internalTransactionDto.setCommissionAmount(calculateCommissionAmount(internalTransactionDto));
        Double fullTransactionAmount = internalTransactionDto.getAmount() + internalTransactionDto.getCommissionAmount();

        // check if sender has enough money for the transaction
        if (sender.getAccountBalance() < fullTransactionAmount)
            throw new IllegalArgumentException("Not enough money on user account");

        // add timestamp to the transaction
        internalTransactionDto.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));

        // update account balance of users
        userService.removeFromAccountBalance(sender, fullTransactionAmount);
        userService.addToAccountBalance(receiver, internalTransactionDto.getAmount());

        // save transaction to database
        return (InternalTransactionDto) transactionService.addTransaction(internalTransactionDto);

    }

    public ExternalTransactionDto addExternalTransaction(ExternalTransactionDto externalTransactionDto) {

        if(externalTransactionDto == null || externalTransactionDto.isEmpty())
            throw new IllegalArgumentException("Invalid transaction");

        UserDto sender = userService.findById(externalTransactionDto.getSenderId());

        // get sender from database
        if (sender == null || sender.isEmpty())
            throw new UserNotFoundException("Sender not found");

        // calculate full amount of transaction
        externalTransactionDto.setCommissionAmount(calculateCommissionAmount(externalTransactionDto));
        Double fullTransactionAmount = externalTransactionDto.getAmount() + externalTransactionDto.getCommissionAmount();

        // add timestamp to the transaction
        externalTransactionDto.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));

        if(externalTransactionDto.isToIban()) {

            // check if sender has enough money for the transaction
            if (sender.getAccountBalance() < fullTransactionAmount) {

                throw new IllegalArgumentException("Not enough money on user account");

            } else {

                // update account balance of sender
                userService.removeFromAccountBalance(sender, fullTransactionAmount);

                // save transaction to database
                return (ExternalTransactionDto) transactionService.addTransaction(externalTransactionDto);

            }

        } else {

            // update account balance of sender
            userService.addToAccountBalance(sender, externalTransactionDto.getAmount());

            // save transaction to database
            return (ExternalTransactionDto) transactionService.addTransaction(externalTransactionDto);

        }

    }

    public List<PastTransactionDto> getPastTransactions(UserDto userDto) {

        if(userDto==null || userDto.isEmpty())
            throw new IllegalArgumentException("Invalid user");

        List<PastTransactionDto> result = new ArrayList<>();

        // find every transaction for which userDto is the sender
        List<TransactionDto> transactions = transactionService.findBySenderId(userDto.getId());

        if (transactions != null && !transactions.isEmpty()) {
            for (TransactionDto transactionDto : transactions) {
                if (transactionDto != null && !transactionDto.isEmpty()) {
                    if (transactionDto.isInternalTransaction()) {

                        // add to result a transaction between users where userDto is the sender
                        result.add(new PastTransactionDto(
                                transactionDto.getId(),
                                userService.findById(((InternalTransactionDto) transactionDto).getReceiverId()).getUsername(),
                                transactionDto.getDescription(),
                                -transactionDto.getAmount(),
                                currencyService.findById(transactionDto.getCurrencyId()).getSymbol()));

                    } else if (transactionDto.isExternalTransaction()) {
                        if (((ExternalTransactionDto) transactionDto).isToIban()) {

                            // add to result a transaction to userDto's bank account
                            result.add(new PastTransactionDto(
                                    transactionDto.getId(),
                                    userService.findById(transactionDto.getSenderId()).getUsername(),
                                    transactionDto.getDescription(),
                                    -transactionDto.getAmount(),
                                    currencyService.findById(transactionDto.getCurrencyId()).getSymbol()));

                        } else {

                            // add to result a transaction from userDto's bank account
                            result.add(new PastTransactionDto(
                                    transactionDto.getId(),
                                    userService.findById(transactionDto.getSenderId()).getUsername(),
                                    transactionDto.getDescription(),
                                    transactionDto.getAmount(),
                                    currencyService.findById(transactionDto.getCurrencyId()).getSymbol()));

                        }
                    }
                }
            }
        }

        // find every transaction for which userDto is the receiver
        transactions = transactionService.findByReceiverId(userDto.getId());

        if (transactions != null && !transactions.isEmpty()) {
            for (TransactionDto transactionDto : transactions) {
                if (transactionDto != null && !transactionDto.isEmpty() && transactionDto.isInternalTransaction()) {

                    // add to result a transaction between users where userDto is the receiver
                    result.add(new PastTransactionDto(
                            transactionDto.getId(),
                            userService.findById(transactionDto.getSenderId()).getUsername(),
                            transactionDto.getDescription(),
                            transactionDto.getAmount(),
                            currencyService.findById(transactionDto.getCurrencyId()).getSymbol()));

                }
            }
        }

        // sort transactions so the most recent one is displayed first
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
