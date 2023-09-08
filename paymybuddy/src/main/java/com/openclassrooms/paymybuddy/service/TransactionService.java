package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.entity.Commission;
import com.openclassrooms.paymybuddy.entity.Currency;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    @Autowired
    CommissionService commissionService;

    @Autowired
    CurrencyService currencyService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserService userService;

    private String externalToIbanDescription = "to bank account";
    private String externalFromIbanDescription = "from bank account";

    public String getExternalToIbanDescription() {
        return externalToIbanDescription;
    }

    public String getExternalFromIbanDescription() {
        return externalFromIbanDescription;
    }

    public TransactionDto findById(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        Transaction transaction = transactionRepository.findById(id);
        if(transaction!=null && !transaction.isEmpty()) {
            if (transaction.isInternalTransaction() && !transaction.isExternalTransaction())
                return new InternalTransactionDto(transaction);
            if (transaction.isExternalTransaction() && !transaction.isInternalTransaction())
                return new ExternalTransactionDto(transaction);
            throw new IllegalArgumentException("Invalid transaction");
        }
        return null;
    }

    public List<TransactionDto> findBySenderId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        List<TransactionDto> result = new ArrayList<>();
        List<Transaction> transactions = transactionRepository.findBySenderId(id);
        if(transactions!=null && !transactions.isEmpty()) {
            for (Transaction transaction : transactions)
                if(transaction!=null && !transaction.isEmpty()) {
                    if (transaction.isInternalTransaction() && !transaction.isExternalTransaction())
                        result.add(new InternalTransactionDto(transaction));
                    else if (transaction.isExternalTransaction() && !transaction.isInternalTransaction())
                        result.add(new ExternalTransactionDto(transaction));
                    else throw new IllegalArgumentException("Invalid transaction");
                }
        }
        return result;
    }

    public List<TransactionDto> findByReceiverId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        List<TransactionDto> result = new ArrayList<>();
        List<Transaction> transactions = transactionRepository.findByReceiverId(id);
        if(transactions!=null && !transactions.isEmpty()) {
            for (Transaction transaction : transactions)
                if(transaction!=null && !transaction.isEmpty()) {
                    if (transaction.isInternalTransaction() && !transaction.isExternalTransaction())
                        result.add(new InternalTransactionDto(transaction));
                    else if (transaction.isExternalTransaction() && !transaction.isInternalTransaction())
                        result.add(new ExternalTransactionDto(transaction));
                    else throw new IllegalArgumentException("Invalid transaction");
                }
        }
        return result;
    }
}
