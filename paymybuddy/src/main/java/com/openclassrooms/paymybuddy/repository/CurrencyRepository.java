package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findById(int id);

}