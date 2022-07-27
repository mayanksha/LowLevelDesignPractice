package com.practice.lld.model;

import lombok.Getter;

@Getter
public class Vehicle {

    public enum Color {
        WHITE("WHITE"),
        ORANGE("ORANGE"),
        RED("RED"),
        BLUE("BLUE"),
        GREEN("GREEN"),
        GRAY("GRAY"),
        PINK("PINK"),
        BLACK("BLACK");

        @Getter
        private final String value;

        private Color(final String value) {
            this.value = value;
        }
    };

    public static enum Type {
        CAR("CAR"),
        BIKE("BIKE"),
        TRUCK("TRUCK");

        @Getter
        private final String value;

        private Type(final String value) {
            this.value = value;
        }
    }

    private final String regNumber;

    private Vehicle.Color color;

    private Vehicle.Type type;

    public Vehicle(final String regNo, Vehicle.Color color, Vehicle.Type type) {
        this.regNumber = regNo;
        this.color = color;
        this.type = type;
    }
}
