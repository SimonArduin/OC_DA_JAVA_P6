package com.openclassrooms.paymybuddy.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userNotFound) {
    }
}
