package com.practice.lld.splitwise.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class User {
    private String Id;
    private String name;
    private String email;
    private String phoneNumber;

    public User(String name) {
        this.Id = name;
        this.name = name;
    }

    public User(String name, String email, String phoneNumber) {
//        this.Id = UUID.randomUUID().toString();

        this.Id = name;

        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
