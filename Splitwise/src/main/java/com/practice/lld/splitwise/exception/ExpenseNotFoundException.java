package com.practice.lld.splitwise.exception;

public class ExpenseNotFoundException extends Exception {
    public ExpenseNotFoundException(final String expenseId) {
        super(String.format("Expense with the given ID is not found in DB. expenseId: %s", expenseId));
    }
}
