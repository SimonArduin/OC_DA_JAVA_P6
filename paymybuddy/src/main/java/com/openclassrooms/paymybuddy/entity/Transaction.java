package com.openclassrooms.paymybuddy.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double commission;

    @Column(name = "currency_id", nullable = false)
    private int currencyId;

    @Column
    private String description;

    @Column(length = 34)
    private String iban;

    @Column(name = "receiver_id")
    private Integer receiverId;

    @Column(name = "sender_id", nullable = false)
    private Integer senderId;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(name = "to_iban")
    private boolean toIban;

    public Transaction() {
    }

    public Transaction(int id, Double amount, Double commission, int currencyId, String description, String iban, Integer receiverId, Integer senderId, Timestamp timestamp, boolean toIban) {
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
        return(this.amount==null || this.senderId==null || this.receiverId==null);
    }
}
