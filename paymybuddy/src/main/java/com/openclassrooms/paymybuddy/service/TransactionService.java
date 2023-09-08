package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.MANDATORY)
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

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
<<<<<<< HEAD
                if(transaction!=null && !transaction.isEmpty()) {
                    if (transaction.isInternalTransaction() && !transaction.isExternalTransaction())
                        result.add(new InternalTransactionDto(transaction));
                    else if (transaction.isExternalTransaction() && !transaction.isInternalTransaction())
                        result.add(new ExternalTransactionDto(transaction));
                    else throw new IllegalArgumentException("Invalid transaction");
                }
        }
=======
                result.add(new DatabaseTransactionDto(transaction));
        }
        return result;
    }

    public List<PastTransactionDto> getPastTransactions(UserDto userDto) {
        if(userDto==null || userDto.isEmpty())
            throw new IllegalArgumentException("Invalid user");
        List<PastTransactionDto> result = new ArrayList<>();
        for (DatabaseTransactionDto databaseTransactionDto : findBySenderId(userDto.getId())) {
            if(databaseTransactionDto!=null && !databaseTransactionDto.isEmpty()) {
                if(databaseTransactionDto.isInternalTransaction()) {
                    result.add(new PastTransactionDto(databaseTransactionDto.getId(),
                            userService.findById(databaseTransactionDto.getReceiverId()).getUsername(),
                            databaseTransactionDto.getDescription(),
                            -databaseTransactionDto.getAmount(),
                            currencyService.findById(databaseTransactionDto.getCurrencyId()).getSymbol()));
                }
                else if (databaseTransactionDto.isExternalTransaction()) {
                    if (databaseTransactionDto.isToIban()) {
                        result.add(new PastTransactionDto(databaseTransactionDto.getId(),
                                userService.findById(databaseTransactionDto.getSenderId()).getUsername(),
                                externalToIbanDescription,
                                -databaseTransactionDto.getAmount(),
                                currencyService.findById(databaseTransactionDto.getCurrencyId()).getSymbol()));
                    }
                    else {
                        result.add(new PastTransactionDto(databaseTransactionDto.getId(),
                                userService.findById(databaseTransactionDto.getSenderId()).getUsername(),
                                externalFromIbanDescription,
                                databaseTransactionDto.getAmount(),
                                currencyService.findById(databaseTransactionDto.getCurrencyId()).getSymbol()));
                    }
                }
            }
        }
        for (DatabaseTransactionDto databaseTransactionDto : findByReceiverId(userDto.getId())) {
            if(databaseTransactionDto!=null && !databaseTransactionDto.isEmpty() && databaseTransactionDto.isInternalTransaction()) {
                result.add(new PastTransactionDto(databaseTransactionDto.getId(),
                        userService.findById(databaseTransactionDto.getSenderId()).getUsername(),
                        databaseTransactionDto.getDescription(),
                        databaseTransactionDto.getAmount(),
                        currencyService.findById(databaseTransactionDto.getCurrencyId()).getSymbol()));
            }
        }
        result.sort(Comparator.comparing(PastTransactionDto::getId).reversed());
>>>>>>> d5340b71596ccbbbebf9b75ecfa0455d0253513c
        return result;
    }
}
