package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public Transaction findById(int id);

    public Transaction findBySender(int id);

    public Transaction findByReceiver(int id);
}