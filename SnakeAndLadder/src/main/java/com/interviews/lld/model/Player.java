package com.interviews.lld.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Player {
    String name;

    public Player(String name) {
        this.name = name;
    }
}
