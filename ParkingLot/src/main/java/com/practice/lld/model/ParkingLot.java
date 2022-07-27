package com.practice.lld.model;

import lombok.Getter;

import java.util.List;

@Getter
public class ParkingLot {
    private final String ID;

    // A Parking Lot should have a bunch of Floors
    public final List<Floor> floorList;

    public ParkingLot(String ID, List<Floor> floorList) {
        this.ID = ID;
        this.floorList = floorList;
    }
}
