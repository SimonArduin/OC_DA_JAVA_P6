package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.entity.Transaction;

import java.sql.Timestamp;

public class TransactionDto {

    private Integer id;

    private Double amount;

    private Double commission;

    private Integer currencyId = 1;

    private String description;

    private String iban;

    private Integer receiverId;

    private Integer senderId;

    private Timestamp timestamp;

    private boolean toIban;

    public TransactionDto() {
    }

    public TransactionDto(Integer id, Double amount, Double commission, Integer currencyId, String description, String iban, Integer receiverId, Integer senderId, Timestamp timestamp, boolean toIban) {
        this.id = id;
        this.amount = amount;
        this.commission = commission;
        this.currencyId = currencyId;
        this.description = description;
        this.iban = iban;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.toIban = toIban;
    }

    public TransactionDto(Transaction transaction) {
        if (transaction == null || transaction.isEmpty())
            throw new IllegalArgumentException();
        else {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.commission = transaction.getCommission();
            this.currencyId = transaction.getCurrencyId();
            this.description = transaction.getDescription();
            this.iban = transaction.getIban();
            this.receiverId = transaction.getReceiverId();
            this.senderId = transaction.getSenderId();
            this.timestamp = transaction.getTimestamp();
            this.toIban = transaction.isToIban();
        }
    }

    public TransactionDto(TransactionDto transactionDto) {
        if (transactionDto == null || transactionDto.isEmpty())
            throw new IllegalArgumentException();
        else {
            this.id = transactionDto.getId();
            this.amount = transactionDto.getAmount();
            this.commission = transactionDto.getCommission();
            this.currencyId = transactionDto.getCurrencyId();
            this.description = transactionDto.getDescription();
            this.iban = transactionDto.getIban();
            this.receiverId = transactionDto.getReceiverId();
            this.senderId = transactionDto.getSenderId();
            this.timestamp = transactionDto.getTimestamp();
            this.toIban = transactionDto.isToIban();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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
                || this.receiverId == null);
    }

    public void calculateCommission() {
        commission = 0.05*amount;
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
        TransactionDto objTransactionDto = (TransactionDto) obj;
        if (objTransactionDto.getId() == this.getId()
                && objTransactionDto.getAmount() == this.getAmount()
                && objTransactionDto.getCommission() == this.getCommission()
                && objTransactionDto.getCurrencyId() == this.getCurrencyId()
                && objTransactionDto.getDescription() == this.getDescription()
                && objTransactionDto.getIban() == this.getIban()
                && objTransactionDto.getReceiverId() == this.getReceiverId()
                && objTransactionDto.getSenderId() == this.getSenderId()
                && objTransactionDto.getTimestamp() == this.getTimestamp()
                && objTransactionDto.isToIban() == this.isToIban()) {
                return true;
        }
        return false;
    }
}
