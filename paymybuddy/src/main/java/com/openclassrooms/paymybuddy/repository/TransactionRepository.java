package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public Transaction findById(int id);

    public List<Transaction> findBySenderId(int id);

    public List<Transaction> findByReceiverId(int id);
}