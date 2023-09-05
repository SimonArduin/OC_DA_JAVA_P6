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
        this.commissionAmount = commission;
        this.currencyId = currencyId;
        this.description = description;
        this.iban = iban;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.toIban = toIban;
    }

    public ExternalTransactionDto(Transaction transaction) {
        if (transaction == null
                || transaction.isEmpty()
                || !transaction.isExternalTransaction())
            throw new IllegalArgumentException();
        else {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.commissionAmount = transaction.getCommissionAmount();
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
            this.commissionAmount = transactionDto.getCommissionAmount();
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
        if (this.isEmpty()) {
            if (objTransactionDto.isEmpty())
                return true;
            return false;
        } else if (this.getId().equals(objTransactionDto.getId())
                && this.getAmount().equals(objTransactionDto.getAmount())
                && this.getCommissionAmount().equals(objTransactionDto.getCommissionAmount())
                && this.getCurrencyId().equals(objTransactionDto.getCurrencyId())
                && this.getDescription().equals(objTransactionDto.getDescription())
                && this.getIban().equals(objTransactionDto.getIban())
                && this.getSenderId().equals(objTransactionDto.getSenderId())
                && this.getTimestamp().equals(objTransactionDto.getTimestamp())
                && this.isToIban() == (objTransactionDto.isToIban())) {
            return true;
        }
        return false;
    }
}
