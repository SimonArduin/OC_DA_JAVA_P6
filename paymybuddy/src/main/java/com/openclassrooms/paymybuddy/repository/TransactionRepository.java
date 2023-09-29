package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findById(int id);

    List<Transaction> findBySenderId(int id);

    List<Transaction> findByReceiverId(int id);
}