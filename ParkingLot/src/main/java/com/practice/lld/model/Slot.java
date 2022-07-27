package com.practice.lld.model;

import lombok.Getter;

public class Slot implements Comparable<Slot> {
    @Override
    public int compareTo(Slot slot) {
        return this.getID().compareTo(slot.getID());
    }

    public static enum Size {
        COMPACT("COMPACT"),
        NORMAL("NORMAL"),
        LARGE("LARGE");

        private final String value;

        private Size(final String value) {
            this.value = value;
        }
    }

//    @Getter
//    private final double perHrRate;

    @Getter
    private final Slot.Size size;

    @Getter
    private final Integer ID;

    public Slot(Integer ID, Size size) {
//        this.perHrRate = perHrRate;
        this.size = size;
        this.ID = ID;
    }

}
