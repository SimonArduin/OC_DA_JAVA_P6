package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public Transaction addTransaction(Transaction transaction) {
        if(transaction == null || transaction.isEmpty())
            throw new IllegalArgumentException();
        return transactionRepository.save(transaction);
    }

    public Transaction findById(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        return transactionRepository.findById(id);
    }

    public Transaction findBySenderId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        return transactionRepository.findBySender(id);
    }

    public Transaction findByReceiverId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        return transactionRepository.findByReceiver(id);
    }
}
