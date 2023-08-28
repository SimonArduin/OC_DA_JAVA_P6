package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserService userService;

    public TransactionDto addTransaction(TransactionDto transactionDto) {
        if(transactionDto == null || transactionDto.isEmpty())
            throw new IllegalArgumentException();
        UserDto sender = userService.findById(transactionDto.getSenderId());
        UserDto receiver = userService.findById(transactionDto.getReceiverId());
        transactionDto.calculateCommission();
        Double fullTransactionAmount = transactionDto.getAmount() + transactionDto.getCommission();
        if(sender!=null && receiver!=null && sender.getAccount_balance()>=(fullTransactionAmount)) {
            transactionDto.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
            Transaction transaction = new Transaction(transactionDto);
            userService.removeFromAccountBalance(sender, fullTransactionAmount);
            userService.addToAccountBalance(receiver, fullTransactionAmount);
            return new TransactionDto(transactionRepository.save(transaction));
        }
        return null;
    }

    public TransactionDto findById(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        Transaction transaction = transactionRepository.findById(id);
        if(transaction!=null && !transaction.isEmpty())
            return new TransactionDto(transaction);
        return null;
    }

    public TransactionDto findBySenderId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        Transaction transaction = transactionRepository.findBySenderId(id);
        if(transaction!=null && !transaction.isEmpty())
            return new TransactionDto(transaction);
        return null;
    }

    public TransactionDto findByReceiverId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        Transaction transaction = transactionRepository.findByReceiverId(id);
        if(transaction!=null && !transaction.isEmpty())
            return new TransactionDto(transaction);
        return null;
    }
}
