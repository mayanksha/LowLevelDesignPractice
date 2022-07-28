package com.practice.lld.splitwise.exception;

public class DuplicateExpenseException extends Exception {
    public DuplicateExpenseException(final String expenseId) {
        super(String.format("Expense with the given ID already exists in the DB. expenseId: %s", expenseId));
    }
}
