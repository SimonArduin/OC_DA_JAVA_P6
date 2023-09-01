package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    CurrencyService currencyService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserService userService;

    public InternalTransactionDto addInternalTransaction(InternalTransactionDto transactionDto) {
        if(transactionDto == null || transactionDto.isEmpty())
            throw new IllegalArgumentException();
        UserDto sender = userService.findById(transactionDto.getSenderId());
        UserDto receiver = userService.findById(transactionDto.getReceiverId());
        transactionDto.calculateCommission();
        Double fullTransactionAmount = transactionDto.getAmount() + transactionDto.getCommission();
        if(sender!=null && receiver!=null && sender.getAccountBalance()>=(fullTransactionAmount)) {
            transactionDto.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
            Transaction transaction = new Transaction(transactionDto);
            userService.removeFromAccountBalance(sender, fullTransactionAmount);
            userService.addToAccountBalance(receiver, transaction.getAmount());
            return new InternalTransactionDto(transactionRepository.save(transaction));
        }
        return null;
    }

    public ExternalTransactionDto addExternalTransaction(ExternalTransactionDto transactionDto) {
        if(transactionDto == null
                || transactionDto.getSenderId() == null
                || transactionDto.getIban() == null
                || transactionDto.getAmount() == null
                || transactionDto.getAmount() < 0)
            throw new IllegalArgumentException();
        UserDto sender = userService.findById(transactionDto.getSenderId());
        if (sender == null || sender.isEmpty())
            return null;
        transactionDto.calculateCommission();
        Double fullTransactionAmount = transactionDto.getAmount() + transactionDto.getCommission();
        transactionDto.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
        Transaction transaction = new Transaction(transactionDto);
        if(transactionDto.isToIban() && sender.getAccountBalance()>=(fullTransactionAmount)) {
            userService.removeFromAccountBalance(sender, fullTransactionAmount);
            return new ExternalTransactionDto(transactionRepository.save(transaction));
        }
        if(!transactionDto.isToIban()) {
            userService.addToAccountBalance(sender, transaction.getAmount());
            return new ExternalTransactionDto(transactionRepository.save(transaction));
        }
        return null;
    }

    public DatabaseTransactionDto findById(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        Transaction transaction = transactionRepository.findById(id);
        if(transaction!=null && !transaction.isEmpty())
            return new DatabaseTransactionDto(transaction);
        return null;
    }

    public List<DatabaseTransactionDto> findBySenderId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        List<DatabaseTransactionDto> result = new ArrayList<DatabaseTransactionDto>();
        List<Transaction> transactions = transactionRepository.findBySenderId(id);
        if(transactions!=null && !transactions.isEmpty()) {
            for (Transaction transaction : transactions)
                result.add(new DatabaseTransactionDto(transaction));
        }
        return result;
    }

    public List<DatabaseTransactionDto> findByReceiverId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        List<DatabaseTransactionDto> result = new ArrayList<DatabaseTransactionDto>();
        List<Transaction> transactions = transactionRepository.findByReceiverId(id);
        if(transactions!=null && !transactions.isEmpty()) {
            for (Transaction transaction : transactions)
                result.add(new DatabaseTransactionDto(transaction));
        }
        return result;
    }

    public List<PastTransactionDto> getPastTransactions(UserDto userDto) {
        if(userDto==null || userDto.isEmpty())
            throw new IllegalArgumentException("Invalid user");
        List<PastTransactionDto> result = new ArrayList<>();
        for (DatabaseTransactionDto transactionDto : findBySenderId(userDto.getId())) {
            if(transactionDto!=null && !transactionDto.isEmpty()) {
                result.add(new PastTransactionDto(transactionDto.getId(),
                        userService.findById(transactionDto.getReceiverId()).getUsername(),
                        transactionDto.getDescription(),
                        -transactionDto.getAmount(),
                        currencyService.findById(transactionDto.getCurrencyId()).getName()));
            }
        }
        for (DatabaseTransactionDto transactionDto : findByReceiverId(userDto.getId())) {
            if(transactionDto!=null && !transactionDto.isEmpty()) {
                result.add(new PastTransactionDto(transactionDto.getId(),
                        userService.findById(transactionDto.getSenderId()).getUsername(),
                        transactionDto.getDescription(),
                        transactionDto.getAmount(),
                        currencyService.findById(transactionDto.getCurrencyId()).getName()));
            }
        }
        result.sort(Comparator.comparing(PastTransactionDto::getId).reversed());
        return result;
    }
}
