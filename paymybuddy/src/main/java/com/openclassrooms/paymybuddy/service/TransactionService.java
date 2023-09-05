package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.*;
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

    public InternalTransactionDto addInternalTransaction(InternalTransactionDto transactionDto) {
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
            return new InternalTransactionDto(transactionRepository.save(transaction));
        }
        return null;
    }

    public ExternalTransactionDto addExternalTransaction(ExternalTransactionDto externalTransactionDto) {
        if(externalTransactionDto == null
                || externalTransactionDto.isEmpty())
            throw new IllegalArgumentException();
        UserDto sender = userService.findById(externalTransactionDto.getSenderId());
        if (sender == null || sender.isEmpty())
            return null;
        externalTransactionDto.calculateCommission();
        Double fullTransactionAmount = externalTransactionDto.getAmount() + externalTransactionDto.getCommission();
        externalTransactionDto.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
        Transaction transaction = new Transaction(externalTransactionDto);
        if(externalTransactionDto.isToIban() && sender.getAccountBalance()>=(fullTransactionAmount)) {
            userService.removeFromAccountBalance(sender, fullTransactionAmount);
            return new ExternalTransactionDto(transactionRepository.save(transaction));
        }
        if(!externalTransactionDto.isToIban()) {
            userService.addToAccountBalance(sender, transaction.getAmount());
            return new ExternalTransactionDto(transactionRepository.save(transaction));
        }
        return null;
    }

    public DatabaseTransactionDto findById(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        Transaction transaction = transactionRepository.findById(id);
        if(transaction!=null && !transaction.isEmpty())
            return new DatabaseTransactionDto(transaction);
        return null;
    }

    public List<DatabaseTransactionDto> findBySenderId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        List<DatabaseTransactionDto> result = new ArrayList<DatabaseTransactionDto>();
        List<Transaction> transactions = transactionRepository.findBySenderId(id);
        if(transactions!=null && !transactions.isEmpty()) {
            for (Transaction transaction : transactions)
                result.add(new DatabaseTransactionDto(transaction));
        }
        return result;
    }

    public List<DatabaseTransactionDto> findByReceiverId(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        List<DatabaseTransactionDto> result = new ArrayList<DatabaseTransactionDto>();
        List<Transaction> transactions = transactionRepository.findByReceiverId(id);
        if(transactions!=null && !transactions.isEmpty()) {
            for (Transaction transaction : transactions)
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
                            currencyService.findById(databaseTransactionDto.getCurrencyId()).getName()));
                }
                else if (databaseTransactionDto.isExternalTransaction()) {
                    if (databaseTransactionDto.isToIban()) {
                        result.add(new PastTransactionDto(databaseTransactionDto.getId(),
                                userService.findById(databaseTransactionDto.getSenderId()).getUsername(),
                                externalToIbanDescription,
                                -databaseTransactionDto.getAmount(),
                                currencyService.findById(databaseTransactionDto.getCurrencyId()).getName()));
                    }
                    else {
                        result.add(new PastTransactionDto(databaseTransactionDto.getId(),
                                userService.findById(databaseTransactionDto.getSenderId()).getUsername(),
                                externalFromIbanDescription,
                                databaseTransactionDto.getAmount(),
                                currencyService.findById(databaseTransactionDto.getCurrencyId()).getName()));
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
                        currencyService.findById(databaseTransactionDto.getCurrencyId()).getName()));
            }
        }
        result.sort(Comparator.comparing(PastTransactionDto::getId).reversed());
        return result;
    }
}
