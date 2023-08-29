package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.PastTransactionDto;
import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    CurrencyService currencyService;

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
        if(sender!=null && receiver!=null && sender.getAccountBalance()>=(fullTransactionAmount)) {
            transactionDto.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
            Transaction transaction = new Transaction(transactionDto);
            userService.removeFromAccountBalance(sender, fullTransactionAmount);
            userService.addToAccountBalance(receiver, transaction.getAmount());
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

    public List<TransactionDto> findBySenderId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        List<TransactionDto> result = new ArrayList<TransactionDto>();
        List<Transaction> transactions = transactionRepository.findBySenderId(id);
        if(transactions!=null && !transactions.isEmpty()) {
            for (Transaction transaction : transactions)
                result.add(new TransactionDto(transaction));
        }
        return result;
    }

    public List<TransactionDto> findByReceiverId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        List<TransactionDto> result = new ArrayList<TransactionDto>();
        List<Transaction> transactions = transactionRepository.findByReceiverId(id);
        if(transactions!=null && !transactions.isEmpty()) {
            for (Transaction transaction : transactions)
                result.add(new TransactionDto(transaction));
        }
        return result;
    }

    public List<PastTransactionDto> getPastTransactions(UserDto userDto) {
        if(userDto==null || userDto.isEmpty())
            throw new IllegalArgumentException("Invalid user");
        List<PastTransactionDto> result = new ArrayList<>();
        for (TransactionDto transactionDto : findBySenderId(userDto.getId())) {
            if(transactionDto!=null && !transactionDto.isEmpty()) {
                result.add(new PastTransactionDto(transactionDto.getId(),
                        userService.findById(transactionDto.getReceiverId()).getUsername(),
                        transactionDto.getDescription(),
                        -transactionDto.getAmount(),
                        currencyService.findById(transactionDto.getCurrencyId()).getName()));
            }
        }
        for (TransactionDto transactionDto : findByReceiverId(userDto.getId())) {
            if(transactionDto!=null && !transactionDto.isEmpty()) {
                result.add(new PastTransactionDto(transactionDto.getId(),
                        userService.findById(transactionDto.getSenderId()).getUsername(),
                        transactionDto.getDescription(),
                        transactionDto.getAmount(),
                        currencyService.findById(transactionDto.getCurrencyId()).getName()));
            }
        }
        result.sort(Comparator.comparing(PastTransactionDto::getId).reversed());
        return result;
    }
}
