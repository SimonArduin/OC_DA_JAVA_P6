package com.openclassrooms.paymybuddy.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String transactionNotFound) {
    }
}
