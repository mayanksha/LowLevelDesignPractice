package com.practice.lld.splitwise.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
public class Contribution {
    private String Id;

    @Setter
    private Date paidOn;
    private Double paidAmount;
    private String paidBy;
    private String paidTo;

    public Contribution(Date paidOn, Double paidAmount, String paidBy, String paidTo) {
        this.Id = UUID.randomUUID().toString();

        this.paidOn = paidOn;
        this.paidAmount = paidAmount;
        this.paidBy = paidBy;
        this.paidTo = paidTo;
    }
}
