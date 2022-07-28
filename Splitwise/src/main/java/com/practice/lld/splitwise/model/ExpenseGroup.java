package com.practice.lld.splitwise.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class ExpenseGroup {
    private String Id;
    private String name;
    private String createdBy;
    private Date createdOn;
    private List<String> expenseIds;

    public void addExpense(String expenseId) {
        this.expenseIds.add(expenseId);
    }
}
