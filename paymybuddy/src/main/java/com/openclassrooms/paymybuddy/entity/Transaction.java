package com.openclassrooms.paymybuddy.entity;

import com.openclassrooms.paymybuddy.dto.ExternalTransactionDto;
import com.openclassrooms.paymybuddy.dto.InternalTransactionDto;
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

    @Column(name = "commission_amount", nullable = false)
    private Double commissionAmount;

    @Column(name = "commission_id", nullable = false)
    private Integer commissionId = 1;

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
    private Boolean toIban;

    public Transaction() {
    }

    public Transaction(Integer id, Double amount, Double commissionAmount, Integer commissionId, Integer currencyId, String description, String iban, Integer receiverId, Integer senderId, Timestamp timestamp, Boolean toIban) {
        this.id = id;
        this.amount = amount;
        this.commissionAmount = commissionAmount;
        this.commissionId = commissionId;
        this.currencyId = currencyId;
        this.description = description;
        this.iban = iban;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.toIban = toIban;
    }

    public Transaction(InternalTransactionDto internalTransactionDto) {
        this.id = internalTransactionDto.getId();
        this.amount = internalTransactionDto.getAmount();
        this.commissionAmount = internalTransactionDto.getCommissionAmount();
        this.currencyId = internalTransactionDto.getCurrencyId();
        this.description = internalTransactionDto.getDescription();
        this.receiverId = internalTransactionDto.getReceiverId();
        this.senderId = internalTransactionDto.getSenderId();
        this.timestamp = internalTransactionDto.getTimestamp();
    }

    public Transaction(ExternalTransactionDto externalTransactionDto) {
        this.id = externalTransactionDto.getId();
        this.amount = externalTransactionDto.getAmount();
        this.commissionAmount = externalTransactionDto.getCommissionAmount();
        this.currencyId = externalTransactionDto.getCurrencyId();
        this.description = externalTransactionDto.getDescription();
        this.iban = externalTransactionDto.getIban();
        this.senderId = externalTransactionDto.getSenderId();
        this.timestamp = externalTransactionDto.getTimestamp();
        this.toIban = externalTransactionDto.isToIban();
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

    public Double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Integer getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(Integer commissionId) {
        this.commissionId = commissionId;
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

    public Boolean isToIban() {
        return toIban;
    }

    public void setToIban(Boolean toIban) {
        this.toIban = toIban;
    }

    public Boolean isEmpty() {
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
                && objTransaction.getCommissionId() == this.getCommissionId()
                && objTransaction.getCommissionAmount() == this.getCommissionAmount()
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
