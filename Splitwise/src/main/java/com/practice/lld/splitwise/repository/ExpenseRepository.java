package com.practice.lld.splitwise.repository;

import com.practice.lld.splitwise.exception.DuplicateExpenseException;
import com.practice.lld.splitwise.exception.ExpenseNotFoundException;
import com.practice.lld.splitwise.model.Expense;

import java.util.HashMap;
import java.util.Map;

public class ExpenseRepository {
    private static ExpenseRepository instance;

    private final Map<String, Expense> db;

    private ExpenseRepository() {
        this.db = new HashMap<>();
    }

    public Expense getExpenseForId(String Id) throws ExpenseNotFoundException {
        if (!db.containsKey(Id)) {
            throw new ExpenseNotFoundException(Id);
        }

        return db.get(Id);
    }

    public void insertExpenseForId(String Id, Expense expense) throws DuplicateExpenseException {
        if (db.containsKey(Id)) {
            throw new DuplicateExpenseException(Id);
        }

        db.put(Id, expense);
    }

    public static ExpenseRepository getInstance() {
        if (instance == null) {
            instance = new ExpenseRepository();
        }
        return instance;
    }

}
