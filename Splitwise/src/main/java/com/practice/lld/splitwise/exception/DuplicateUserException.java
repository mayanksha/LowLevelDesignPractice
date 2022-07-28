package com.practice.lld.splitwise.exception;

public class DuplicateUserException extends Exception {
    public DuplicateUserException(final String expenseId) {
        super(String.format("User with the given ID already exists in the DB. expenseId: %s", expenseId));
    }
}
