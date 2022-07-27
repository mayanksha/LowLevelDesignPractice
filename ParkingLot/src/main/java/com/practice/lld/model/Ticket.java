package com.practice.lld.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@EqualsAndHashCode
public class Ticket {
    private final String parkingLotId;

    private final Floor floor;

    private final Slot slot;

    private final Vehicle vehicle;

    private final Date startTime;

    @Setter
    private Date endTime = null;

    public Ticket(String parkingLotId, Floor floor, Slot slot, Vehicle vehicle) {
        this.parkingLotId = parkingLotId;
        this.floor = floor;
        this.slot = slot;
        this.vehicle = vehicle;

        this.startTime = new Date();
    }

    public String getTicketId() {
        return String.format("%s_%s_%s", parkingLotId, floor.getFloorNo(), slot.getID());
    }
}
