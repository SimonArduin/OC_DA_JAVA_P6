package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.entity.Transaction;

import java.sql.Timestamp;

public class DatabaseTransactionDto extends TransactionDto {

    private String iban;

    private Integer receiverId;

    private Boolean toIban;

    private String description;

    public DatabaseTransactionDto() {
    }

    public DatabaseTransactionDto(Integer id, Double amount, Integer commissionId, Double commissionAmount, Integer currencyId, String description, String iban, Integer receiverId, Integer senderId, Timestamp timestamp, boolean toIban) {
        this.id = id;
        this.amount = amount;
        this.commissionId = commissionId;
        this.commissionAmount = commissionAmount;
        this.currencyId = currencyId;
        this.description = description;
        this.iban = iban;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.toIban = toIban;
    }

    public DatabaseTransactionDto(Transaction transaction) {
        if (transaction == null || transaction.isEmpty())
            throw new IllegalArgumentException();
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.commissionId = transaction.getCommissionId();
        this.commissionAmount = transaction.getCommissionAmount();
        this.currencyId = transaction.getCurrencyId();
        this.description = transaction.getDescription();
        this.senderId = transaction.getSenderId();
        this.timestamp = transaction.getTimestamp();
        if (transaction.isInternalTransaction()) {
            this.receiverId = transaction.getReceiverId();
        }
        if (transaction.isExternalTransaction()) {
            this.iban = transaction.getIban();
            this.toIban = transaction.isToIban();
        }
    }

    public DatabaseTransactionDto(DatabaseTransactionDto transactionDto) {
        if (transactionDto == null || transactionDto.isEmpty())
            throw new IllegalArgumentException();
        else {
            this.id = transactionDto.getId();
            this.amount = transactionDto.getAmount();
            this.commissionId = transactionDto.getCommissionId();
            this.commissionAmount = transactionDto.getCommissionAmount();
            this.currencyId = transactionDto.getCurrencyId();
            this.description = transactionDto.getDescription();
            this.iban = transactionDto.getIban();
            this.receiverId = transactionDto.getReceiverId();
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

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Boolean isToIban() {
        return toIban;
    }

    public void setToIban(Boolean toIban) {
        this.toIban = toIban;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEmpty() {
        return(this.amount == null
                || this.commissionId == null
                || this.senderId == null
                || this.currencyId == null
                || this.timestamp == null);
    }

    public Boolean isExternalTransaction() {
        return!(this.iban == null
                || this.toIban == null);
    }

    public Boolean isInternalTransaction() {
        return!(this.receiverId == null);
    }

    public void calculateCommissionAmount() {
        commissionAmount = 0.05*amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return this.isEmpty();
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        DatabaseTransactionDto objDatabaseTransactionDto = (DatabaseTransactionDto) obj;
        if (this.isEmpty()) {
            return objDatabaseTransactionDto.isEmpty();
        } else return this.getId().equals(objDatabaseTransactionDto.getId())
                && this.getAmount().equals(objDatabaseTransactionDto.getAmount())
                && this.getCommissionId().equals(objDatabaseTransactionDto.getCommissionId())
                && this.getCommissionAmount().equals(objDatabaseTransactionDto.getCommissionAmount())
                && this.getCurrencyId().equals(objDatabaseTransactionDto.getCurrencyId())
                && this.getDescription().equals(objDatabaseTransactionDto.getDescription())
                && this.getIban().equals(objDatabaseTransactionDto.getIban())
                && this.getReceiverId().equals(objDatabaseTransactionDto.getReceiverId())
                && this.getSenderId().equals(objDatabaseTransactionDto.getSenderId())
                && this.getTimestamp().equals(objDatabaseTransactionDto.getTimestamp())
                && this.isToIban() == objDatabaseTransactionDto.isToIban();
    }
}