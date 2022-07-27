package com.practice.lld.service;

import com.practice.lld.model.*;
import com.practice.lld.strategy.SlotVehicleMappingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ParkingLotService {

    // Maps the ticket store to the actual Ticket instance
    private Map<String, Ticket> ticketStore;

    private final ParkingLot parkingLot;

    private final SlotVehicleMappingStrategy strategy;

    /**
     * Takes the parkingLot instance and performs all the operations on it and does all the booking
     * @param parkingLot
     * @param strategy
     */
    public ParkingLotService(ParkingLot parkingLot, SlotVehicleMappingStrategy strategy) {
        this.parkingLot = parkingLot;
        this.strategy = strategy;
        this.ticketStore = new HashMap<>();
    }

    /**
     * Checks if there's any space avaiable for given vehicle
     * @param vehicle
     * @return
     */
    public boolean isSpaceAvailableForVehicle(final Vehicle vehicle) throws Exception {
        // We now have a bunch of allowed slot sizes from which we can choose from
        List<Slot.Size> slotSizesForVehicle = strategy.getSlotSizesForVehicleType(vehicle.getType());

        List<Floor> floorList = parkingLot.getFloorList();
        for(Floor floor: floorList) {
            for(Slot.Size slotSize: slotSizesForVehicle) {
                if (!floor.getEmptySlotsOfSize(slotSize).isEmpty())
                    return true;
            }
        }

        return false;
    }

    /**
     * After we've checked for presence of space, we'll get the ticket
     * @param vehicle
     * @return Valid ticket if space is available otherwise null
     */
    public Ticket getTicketForVehicleAndPark(final Vehicle vehicle) throws Exception {
        List<Slot.Size> slotSizesForVehicle = strategy.getSlotSizesForVehicleType(vehicle.getType());

        List<Floor> floorList = parkingLot.getFloorList();
        for(Floor floor: floorList) {
            for(Slot.Size slotSize: slotSizesForVehicle) {
                if (!floor.getEmptySlotsOfSize(slotSize).isEmpty()) {
                    Slot slot = reserveSlotOnFloor(floor, slotSize);

                    Ticket ticket = new Ticket(parkingLot.getID(), floor, slot, vehicle);

                    ticketStore.put(ticket.getTicketId(), ticket);
                    return ticket;
                }
            }
        }

        return null;
    }

    public boolean unparkVehicleWithTicketID(final String ticketID) {
        if (!ticketStore.containsKey(ticketID))
            return false;
        return unparkVehicleWithTicket(ticketStore.get(ticketID));
    }

    public Integer getCountOfEmptySlotsForVehicleType(Vehicle.Type type) throws Exception {
        List<Floor> floorList = parkingLot.getFloorList();
        List<Slot.Size> slotSizesForVehicle = strategy.getSlotSizesForVehicleType(type);

        Integer result = 0;
        for (Floor floor : floorList) {
            Integer curr = 0;
            for (Slot.Size size : slotSizesForVehicle) {
                curr += floor.getEmptySlotsOfSize(size).size();
            }

            log.info("No. of free slots for {} on Floor {}: {}", type.getValue(), floor.getFloorNo().toString(), curr);
            result += curr;
        }

        return result;
    }

    /**
     * Returns the slot IDs on all the floors which are empty and match the vehicle type
     * @param type
     * @return
     * @throws Exception
     */
    public List<Integer> getListOfFilledSlotsForVehicleType(Vehicle.Type type) throws Exception {
        List<Floor> floorList = parkingLot.getFloorList();
        List<Slot.Size> slotSizesForVehicle = strategy.getSlotSizesForVehicleType(type);

        List<Integer> result = new ArrayList<>();
        for (Floor floor : floorList) {
            List<Integer> curr = new ArrayList<>();
            for (Slot.Size size : slotSizesForVehicle) {
                curr.addAll(floor.getFilledSlotsOfSize(size).stream().map(e -> e.getID()).collect(Collectors.toList()));
            }

            log.info("Occupied slots for {} on Floor {}: {}", type, floor.getFloorNo(), curr);
            result.addAll(curr);
        }

        return result;
    }

    /**
     * Returns the slot IDs on all the floors which are empty and match the vehicle type
     * @param type
     * @return
     * @throws Exception
     */
    public List<Integer> getListOfEmptySlotsForVehicleType(Vehicle.Type type) throws Exception {
        List<Floor> floorList = parkingLot.getFloorList();
        List<Slot.Size> slotSizesForVehicle = strategy.getSlotSizesForVehicleType(type);

        List<Integer> result = new ArrayList<>();
        for (Floor floor : floorList) {
            List<Integer> curr = new ArrayList<>();
            for (Slot.Size size : slotSizesForVehicle) {
                curr.addAll(floor.getEmptySlotsOfSize(size).stream().map(e -> e.getID()).collect(Collectors.toList()));
            }

            log.info("Free slots for {} on Floor {}: {}", type, floor.getFloorNo(), curr);
            result.addAll(curr);
        }

        return result;
    }

    public List<Slot> getAllFreeSlotsOnFloor(Floor floor) {
        List<Slot> allEmptySlots = new ArrayList<>();

        for (Map.Entry<Slot.Size, Set<Slot>> entry : floor.getEmptySlots().entrySet()) {
            for(Slot slot: entry.getValue()) {
                allEmptySlots.add(slot);
            }
        }

        return allEmptySlots;
    }

    public List<Slot> getAllFreeSlotsForVehicleOnFloor(Floor floor, Vehicle vehicle) throws Exception {
        List<Slot.Size> slotSizesForVehicle = strategy.getSlotSizesForVehicleType(vehicle.getType());
        List<Slot> emptySlots = new ArrayList<>();

        for (Slot.Size size : slotSizesForVehicle) {
            for (Slot slot : floor.getEmptySlotsOfSize(size)) {
                emptySlots.add(slot);
            }
        }

        return emptySlots;
    }

    public List<Slot> getAllOccupiedSlotsOnFloor(Floor floor) {
        List<Slot> slotList = new ArrayList<>();

        floor.getFilledSlots().forEach((size, slotsSet) -> {
            slotsSet.forEach(slot -> slotList.add(slot));
        });

        return slotList;
    }

    private boolean unparkVehicleWithTicket(final Ticket ticket) {
        // Check if the ticket's redemption has already been done
        if (ticket == null || ticket.getEndTime() != null)
            return false;

        releaseSlotOnFloor(ticket.getFloor(), ticket.getSlot());
        ticket.setEndTime(new Date());

        log.info("Unpark. ticketId: {}, regNo: {}, color: {}", ticket.getTicketId(), ticket.getVehicle().getRegNumber(), ticket.getVehicle().getColor());
        return true;
    }

    private Slot reserveSlotOnFloor(Floor floor, Slot.Size size) {
        Set<Slot> emptySlots = floor.getEmptySlotsOfSize(size);
        if (emptySlots.isEmpty())
            return null;

        // Now we need to do some book keeping
        Set<Slot> filledSlotsOfSize = floor.getFilledSlotsOfSize(size);

        Slot emptySlot = emptySlots.iterator().next();
        emptySlots.remove(emptySlot);
        filledSlotsOfSize.add(emptySlot);

        return emptySlot;
    }

    private void releaseSlotOnFloor(Floor floor, Slot slot) {
        Set<Slot> filledSlotsOfSize = floor.getFilledSlotsOfSize(slot.getSize());
        Set<Slot> emptySlots = floor.getEmptySlotsOfSize(slot.getSize());

        filledSlotsOfSize.remove(slot);
        emptySlots.add(slot);
    }
}
