package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.entity.Transaction;

import java.sql.Timestamp;

public class InternalTransactionDto extends TransactionDto {

    private Integer receiverId;

    public InternalTransactionDto() {
    }

    public InternalTransactionDto(Integer id, Double amount, Integer commissionId, Double commissionAmount, Integer currencyId, String description, Integer receiverId, Integer senderId, Timestamp timestamp) {
        this.id = id;
        this.amount = amount;
        this.commissionId = commissionId;
        this.commissionAmount = commissionAmount;
        this.currencyId = currencyId;
        this.description = description;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public InternalTransactionDto(Transaction transaction) {
        if (transaction == null
                || transaction.isEmpty()
                || !transaction.isInternalTransaction())
            throw new IllegalArgumentException();
        else {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.commissionId = transaction.getCommissionId();
            this.commissionAmount = transaction.getCommissionAmount();
            this.currencyId = transaction.getCurrencyId();
            this.description = transaction.getDescription();
            this.receiverId = transaction.getReceiverId();
            this.senderId = transaction.getSenderId();
            this.timestamp = transaction.getTimestamp();
        }
    }

    public InternalTransactionDto(InternalTransactionDto transactionDto) {
        if (transactionDto == null || transactionDto.isEmpty())
            throw new IllegalArgumentException();
        else {
            this.id = transactionDto.getId();
            this.amount = transactionDto.getAmount();
            this.commissionId = transactionDto.getCommissionId();
            this.commissionAmount = transactionDto.getCommissionAmount();
            this.currencyId = transactionDto.getCurrencyId();
            this.description = transactionDto.getDescription();
            this.receiverId = transactionDto.getReceiverId();
            this.senderId = transactionDto.getSenderId();
            this.timestamp = transactionDto.getTimestamp();
        }
    }
    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public boolean isEmpty() {
        return(this.amount == null
                || this.commissionId == null
                || this.senderId == null
                || this.receiverId == null);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return this.isEmpty();
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        InternalTransactionDto objTransactionDto = (InternalTransactionDto) obj;
        if (this.isEmpty()) {
            return objTransactionDto.isEmpty();
        } else return this.getId().equals(objTransactionDto.getId())
                && this.getAmount().equals(objTransactionDto.getAmount())
                && this.getCommissionId().equals(objTransactionDto.getCommissionId())
                && this.getCommissionAmount().equals(objTransactionDto.getCommissionAmount())
                && this.getCurrencyId().equals(objTransactionDto.getCurrencyId())
                && this.getDescription().equals(objTransactionDto.getDescription())
                && this.getReceiverId().equals(objTransactionDto.getReceiverId())
                && this.getSenderId().equals(objTransactionDto.getSenderId())
                && this.getTimestamp().equals(objTransactionDto.getTimestamp());
    }
}
