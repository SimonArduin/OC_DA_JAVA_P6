package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.entity.Transaction;

public class InternalTransactionDto extends TransactionDto {

    private Integer receiverId;

    private String description;

    public InternalTransactionDto() {
    }

    public InternalTransactionDto(Transaction transaction) {
        if (transaction == null
                || transaction.isEmpty()
                || !transaction.isInternalTransaction())
            throw new IllegalArgumentException("Invalid transaction");
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

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    @Override
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

    @Override
    public boolean isExternalTransaction() {
        return false;
    }

    @Override
    public boolean isInternalTransaction() {
        return true;
    }
}
