package com.openclassrooms.paymybuddy.entity;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double commission;

    @Column(name = "currency_id", nullable = false)
    private Integer currencyId = 1;

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

    public Transaction(Integer id, Double amount, Double commission, Integer currencyId, String description, String iban, Integer receiverId, Integer senderId, Timestamp timestamp, boolean toIban) {
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
    
    public Transaction(TransactionDto transactionDto) {
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
        Transaction objTransaction = (Transaction) obj;
        if (objTransaction.getId() == this.getId()
                && objTransaction.getAmount() == this.getAmount()
                && objTransaction.getCommission() == this.getCommission()
                && objTransaction.getCurrencyId() == this.getCurrencyId()
                && objTransaction.getDescription() == this.getDescription()
                && objTransaction.getIban() == this.getIban()
                && objTransaction.getReceiverId() == this.getReceiverId()
                && objTransaction.getSenderId() == this.getSenderId()
                && objTransaction.getTimestamp() == this.getTimestamp()
                && objTransaction.isToIban() == this.isToIban()) {
            return true;
        }
        return false;
    }
}
