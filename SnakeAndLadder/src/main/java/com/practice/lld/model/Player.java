package com.practice.lld.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Player {
    String name;

    public Player(String name) {
        this.name = name;
    }
}
