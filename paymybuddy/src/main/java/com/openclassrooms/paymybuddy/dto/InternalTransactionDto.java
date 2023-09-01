package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.entity.Transaction;

import java.sql.Timestamp;

public class InternalTransactionDto extends TransactionDto {

    private Integer receiverId;

    public InternalTransactionDto() {
    }

    public InternalTransactionDto(Integer id, Double amount, Double commission, Integer currencyId, String description, Integer receiverId, Integer senderId, Timestamp timestamp) {
        this.id = id;
        this.amount = amount;
        this.commission = commission;
        this.currencyId = currencyId;
        this.description = description;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public InternalTransactionDto(Transaction transaction) {
        if (transaction == null || transaction.isEmpty())
            throw new IllegalArgumentException();
        else {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.commission = transaction.getCommission();
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
            this.commission = transactionDto.getCommission();
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
                || this.senderId == null
                || this.receiverId == null);
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
        InternalTransactionDto objTransactionDto = (InternalTransactionDto) obj;
        if (objTransactionDto.getId() == this.getId()
                && objTransactionDto.getAmount() == this.getAmount()
                && objTransactionDto.getCommission() == this.getCommission()
                && objTransactionDto.getCurrencyId() == this.getCurrencyId()
                && objTransactionDto.getDescription() == this.getDescription()
                && objTransactionDto.getReceiverId() == this.getReceiverId()
                && objTransactionDto.getSenderId() == this.getSenderId()
                && objTransactionDto.getTimestamp() == this.getTimestamp()) {
                return true;
        }
        return false;
    }
}
