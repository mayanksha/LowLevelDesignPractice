package com.practice.lld.model;

import lombok.ToString;

import java.util.Random;

@ToString
public class Dice {
    private final Integer maxValue;

    private final Random random = new Random();

    public Dice(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Integer roll() {
        return random.nextInt(maxValue) + 1;
    }

}
