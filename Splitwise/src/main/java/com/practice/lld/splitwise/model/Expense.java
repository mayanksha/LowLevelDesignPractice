package com.practice.lld.splitwise.model;

import lombok.*;

import java.util.*;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Expense {
    private final String Id = UUID.randomUUID().toString();

    private Date createdOn;
    private Date settledOn;

    @Setter
    private ExpenseStatus status;

    private String description;
    private String createdById;
    private Double amount;
    private Map<String, Double> sharedBy;

    private List<Contribution> contriHistory;
}
