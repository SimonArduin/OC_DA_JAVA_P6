package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.entity.User;
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

    public Transaction addTransaction(TransactionDto transactionDto) {
        if(transactionDto == null || transactionDto.isEmpty())
            throw new IllegalArgumentException();
        User sender = userService.findById(transactionDto.getSenderId());
        User receiver = userService.findById(transactionDto.getReceiverId());
        transactionDto.calculateCommission();
        Double fullTransactionAmount = transactionDto.getAmount() + transactionDto.getCommission();
        if(sender!=null && receiver!=null && sender.getAccount_balance()>=(fullTransactionAmount)) {
            transactionDto.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
            Transaction transaction = new Transaction(transactionDto);
            userService.removeFromAccountBalance(sender, fullTransactionAmount);
            userService.addToAccountBalance(receiver, fullTransactionAmount);
            return transactionRepository.save(transaction);
        }
        return null;
    }

    public Transaction findById(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        return transactionRepository.findById(id);
    }

    public Transaction findBySenderId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        return transactionRepository.findBySenderId(id);
    }

    public Transaction findByReceiverId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        return transactionRepository.findByReceiverId(id);
    }
}
