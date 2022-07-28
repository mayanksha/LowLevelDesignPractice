package com.practice.lld.splitwise.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(final String expenseId) {
        super(String.format("User with the given ID is not found in DB. expenseId: %s", expenseId));
    }
}
