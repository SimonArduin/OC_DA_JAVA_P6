package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.entity.Transaction;

import java.sql.Timestamp;

public class ExternalTransactionDto extends TransactionDto {

    private String iban;

    private boolean toIban;

    public ExternalTransactionDto() {
    }

    public ExternalTransactionDto(Integer id, Double amount, Double commission, Integer currencyId, String description, String iban, Integer senderId, Timestamp timestamp, boolean toIban) {
        this.id = id;
        this.amount = amount;
        this.commission = commission;
        this.currencyId = currencyId;
        this.description = description;
        this.iban = iban;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.toIban = toIban;
    }

    public ExternalTransactionDto(Transaction transaction) {
        if (transaction == null || transaction.isEmpty())
            throw new IllegalArgumentException();
        else {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.commission = transaction.getCommission();
            this.currencyId = transaction.getCurrencyId();
            this.description = transaction.getDescription();
            this.iban = transaction.getIban();
            this.senderId = transaction.getSenderId();
            this.timestamp = transaction.getTimestamp();
            this.toIban = transaction.isToIban();
        }
    }

    public ExternalTransactionDto(ExternalTransactionDto transactionDto) {
        if (transactionDto == null || transactionDto.isEmpty())
            throw new IllegalArgumentException();
        else {
            this.id = transactionDto.getId();
            this.amount = transactionDto.getAmount();
            this.commission = transactionDto.getCommission();
            this.currencyId = transactionDto.getCurrencyId();
            this.description = transactionDto.getDescription();
            this.iban = transactionDto.getIban();
            this.senderId = transactionDto.getSenderId();
            this.timestamp = transactionDto.getTimestamp();
            this.toIban = transactionDto.isToIban();
        }
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public boolean isToIban() {
        return toIban;
    }

    public void setToIban(boolean toIban) {
        this.toIban = toIban;
    }

    public boolean isEmpty() {
        return(this.amount == null
                || this.senderId == null
                || this.iban == null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            if (this.isEmpty())
                return true;
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        ExternalTransactionDto objTransactionDto = (ExternalTransactionDto) obj;
        if (objTransactionDto.getId() == this.getId()
                && objTransactionDto.getAmount() == this.getAmount()
                && objTransactionDto.getCommission() == this.getCommission()
                && objTransactionDto.getCurrencyId() == this.getCurrencyId()
                && objTransactionDto.getDescription() == this.getDescription()
                && objTransactionDto.getIban() == this.getIban()
                && objTransactionDto.getSenderId() == this.getSenderId()
                && objTransactionDto.getTimestamp() == this.getTimestamp()
                && objTransactionDto.isToIban() == this.isToIban()) {
                return true;
        }
        return false;
    }
}
