package com.practice.lld.model;

import lombok.Getter;

import java.util.*;

@Getter
public class Floor {
    // Each floor should have a no. and
    private final Integer floorNo;

    private Map<Slot.Size, Set<Slot>> emptySlots, filledSlots;

    public Floor(Integer floorNo, List<Slot> slotList) {
        this.floorNo = floorNo;

        this.filledSlots = new HashMap<>();
        for(Slot slot: slotList) {
            if (!filledSlots.containsKey(slot.getSize())) {
                filledSlots.put(slot.getSize(), new TreeSet<>());
            }
        }

        this.emptySlots = new HashMap<>();
        for(Slot slot: slotList) {
            if (emptySlots.containsKey(slot.getSize())) {
                emptySlots.get(slot.getSize()).add(slot);
            } else {
                Set<Slot> newSlots = new TreeSet<>();
                newSlots.add(slot);
                emptySlots.put(slot.getSize(), newSlots);
            }
        }
    }

    /**
     * Returns all the available slots for a given size on a given floor
     * @param size
     * @return
     */
    public Set<Slot> getEmptySlotsOfSize(Slot.Size size) {
        return emptySlots.getOrDefault(size, new TreeSet<>());
    }

    public Set<Slot> getFilledSlotsOfSize(Slot.Size size) {
        return filledSlots.getOrDefault(size, new TreeSet<>());
    }

    public Set<Slot> getBookedSlotsOfSize(Slot.Size size) {
        return filledSlots.getOrDefault(size, new TreeSet<>());
    }

}
