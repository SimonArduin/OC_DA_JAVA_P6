package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.entity.Transaction;
import jakarta.persistence.*;

import java.sql.Timestamp;

public class TransactionDto {

    private int id;

    private Double amount;

    private Double commission;

    private int currencyId = 1;

    private String description;

    private String iban;

    private int receiverId;

    private int senderId;

    private Timestamp timestamp;

    private boolean toIban;

    public TransactionDto() {
    }

    public TransactionDto(int id, Double amount, Double commission, int currencyId, String description, String iban, Integer receiverId, Integer senderId, Timestamp timestamp, boolean toIban) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
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

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
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
        return(this.amount==null || this.senderId==0 || this.receiverId==0);
    }

    public void calculateCommission() {
        commission = 0.05*amount;
    }
}