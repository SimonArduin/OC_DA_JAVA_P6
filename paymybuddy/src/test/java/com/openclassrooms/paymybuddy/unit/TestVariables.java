package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.entity.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class TestVariables {

    Role role;
    protected Currency currency;
    protected Commission commission;
    protected User user;
    protected User userOther;
    protected UserDto userDto;
    protected UserDto userDtoOther;
    protected Transaction transaction;
    protected Transaction transactionOther;
    protected Transaction internalTransaction;
    protected Transaction externalTransaction;
    protected List<Transaction> transactionList;
    protected InternalTransactionDto internalTransactionDto;
    protected InternalTransactionDto internalTransactionDtoOther;
    protected ExternalTransactionDto externalTransactionDto;
    protected ExternalTransactionDto externalTransactionDtoOther;
    protected List<TransactionDto> transactionDtoList;
    protected PastTransactionDto pastTransactionDto;
    protected PastTransactionDto pastTransactionDtoOther;
    protected List<PastTransactionDto> pastTransactionDtoList;

    public void initializeVariables() {


        role = new Role(1, "role");

        currency = new Currency(1, "currency", "€");

        commission = new Commission(1, 0.1);

        user = new User(1, 100.00,1,"email","iban","password",1,"username",new ArrayList<>());
        userOther = new User(2, 200.00,1,"emailOtherOther","ibanOther","passwordOther",1,"usernameOther",new ArrayList<>());
        userDto = new UserDto(user);
        userDtoOther = new UserDto(userOther);
        transaction = new Transaction(user.getId(), 10.0, commission.getRate()*10.0, commission.getId(), currency.getId(), "description", userDto.getIban(), 2, userDto.getId(), new Timestamp(0), false);
        transactionOther = new Transaction(userOther.getId(), 20.0, commission.getRate()*20.0, commission.getId(), 2, "descriptionOther", userDtoOther.getIban(), 4, userDtoOther.getId(), new Timestamp(0), true);
        internalTransactionDto = new InternalTransactionDto(transaction);
        internalTransactionDtoOther = new InternalTransactionDto(transactionOther);
        externalTransactionDto = new ExternalTransactionDto(transaction);
        externalTransactionDtoOther = new ExternalTransactionDto(transactionOther);
        transactionDtoList = new ArrayList<>(Arrays.asList(internalTransactionDto, externalTransactionDto));
        internalTransaction = new Transaction(internalTransactionDto);
        externalTransaction = new Transaction(externalTransactionDto);
        transactionList = new ArrayList<>(Arrays.asList(internalTransaction, externalTransaction));
        pastTransactionDto = new PastTransactionDto(internalTransactionDto.getId(), userDto.getUsername(), internalTransactionDto.getDescription(), internalTransactionDto.getAmount(), currency.getName());
        pastTransactionDtoOther = new PastTransactionDto(internalTransactionDtoOther.getId(), userDtoOther.getUsername(), internalTransactionDtoOther.getDescription(), internalTransactionDtoOther.getAmount(), currency.getName());

        pastTransactionDtoList = new ArrayList<>(Arrays.asList(pastTransactionDto, pastTransactionDtoOther));
    }
}
