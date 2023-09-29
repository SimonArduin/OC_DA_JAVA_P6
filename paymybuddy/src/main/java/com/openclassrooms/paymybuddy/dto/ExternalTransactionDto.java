package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.entity.Transaction;

public class ExternalTransactionDto extends TransactionDto {

    private String iban;

    private boolean toIban;

    final String toIbanDescription = "to bank account";
    final String notToIbanDescription = "from bank account";

    public ExternalTransactionDto() {
    }

    public ExternalTransactionDto(Transaction transaction) {
        if (transaction == null
                || transaction.isEmpty()
                || !transaction.isExternalTransaction())
            throw new IllegalArgumentException("Invalid transaction");
        else {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.commissionId = transaction.getCommissionId();
            this.commissionAmount = transaction.getCommissionAmount();
            this.currencyId = transaction.getCurrencyId();
            this.iban = transaction.getIban();
            this.senderId = transaction.getSenderId();
            this.timestamp = transaction.getTimestamp();
            this.toIban = transaction.isToIban();
        }
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public boolean isToIban() {
        return toIban;
    }

    public void setToIban(boolean toIban) {
        this.toIban = toIban;
    }

    public boolean isEmpty() {
        return(this.amount == null
                || this.commissionId == null
                || this.senderId == null
                || this.iban == null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return this.isEmpty();
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        ExternalTransactionDto objTransactionDto = (ExternalTransactionDto) obj;
        if (this.isEmpty()) {
            return objTransactionDto.isEmpty();
        } else return this.getId().equals(objTransactionDto.getId())
                && this.getAmount().equals(objTransactionDto.getAmount())
                && this.getCommissionId().equals(objTransactionDto.getCommissionId())
                && this.getCommissionAmount().equals(objTransactionDto.getCommissionAmount())
                && this.getCurrencyId().equals(objTransactionDto.getCurrencyId())
                && this.getIban().equals(objTransactionDto.getIban())
                && this.getSenderId().equals(objTransactionDto.getSenderId())
                && this.getTimestamp().equals(objTransactionDto.getTimestamp())
                && this.isToIban() == (objTransactionDto.isToIban());
    }

    @Override
    public boolean isExternalTransaction() {
        return true;
    }

    @Override
    public boolean isInternalTransaction() {
        return false;
    }

    @Override
    public String getDescription() {
        if (toIban)
            return toIbanDescription;
        return notToIbanDescription;
    }
}
