package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.Currency;
import com.openclassrooms.paymybuddy.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
    @Autowired
    CurrencyRepository currencyRepository;
    public Currency findById(Integer id) {
        if(id==null)
            throw new IllegalArgumentException("Invalid id");
        Currency result = currencyRepository.findById(id);
        if (result == null)
            throw new IllegalArgumentException("Currency not found");
        return result;
    }
}
