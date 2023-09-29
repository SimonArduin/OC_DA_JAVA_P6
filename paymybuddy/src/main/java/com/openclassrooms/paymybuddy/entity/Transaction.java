package com.openclassrooms.paymybuddy.entity;

import com.openclassrooms.paymybuddy.dto.ExternalTransactionDto;
import com.openclassrooms.paymybuddy.dto.InternalTransactionDto;
import com.openclassrooms.paymybuddy.dto.TransactionDto;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

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

    public Transaction(TransactionDto transactionDto) {
        if(transactionDto == null) {
            throw new IllegalArgumentException("Invalid transaction");
        }
        this.id = transactionDto.getId();
        this.amount = transactionDto.getAmount();
        this.commissionAmount = transactionDto.getCommissionAmount();
        this.currencyId = transactionDto.getCurrencyId();
        this.description = transactionDto.getDescription();
        this.senderId = transactionDto.getSenderId();
        this.timestamp = transactionDto.getTimestamp();
        if (transactionDto.isInternalTransaction()) {
            this.receiverId = ((InternalTransactionDto) transactionDto).getReceiverId();
        }
        else if (transactionDto.isExternalTransaction()) {
            this.iban = ((ExternalTransactionDto) transactionDto).getIban();
            this.toIban = ((ExternalTransactionDto) transactionDto).isToIban();
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
            return this.isEmpty();
        }
        else if (obj.getClass() != this.getClass()) {
            return false;
        }
        Transaction objTransaction = (Transaction) obj;
        return Objects.equals(objTransaction.getId(), this.getId())
                && Objects.equals(objTransaction.getAmount(), this.getAmount())
                && Objects.equals(objTransaction.getCommissionId(), this.getCommissionId())
                && Objects.equals(objTransaction.getCommissionAmount(), this.getCommissionAmount())
                && Objects.equals(objTransaction.getCurrencyId(), this.getCurrencyId())
                && Objects.equals(objTransaction.getDescription(), this.getDescription())
                && Objects.equals(objTransaction.getIban(), this.getIban())
                && Objects.equals(objTransaction.getReceiverId(), this.getReceiverId())
                && Objects.equals(objTransaction.getSenderId(), this.getSenderId())
                && Objects.equals(objTransaction.getTimestamp(), this.getTimestamp())
                && Objects.equals(objTransaction.isToIban(), this.isToIban());
    }
}
