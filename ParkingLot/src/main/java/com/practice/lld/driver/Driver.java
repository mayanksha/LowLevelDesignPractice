package com.practice.lld.driver;

import com.practice.lld.model.*;
import com.practice.lld.service.ParkingLotService;
import com.practice.lld.strategy.ExactMatchSlotVehicleMappingStrategy;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
@NoArgsConstructor
public class Driver {

    private void handleDisplay(Scanner scanner, ParkingLotService parkingLotService) throws Exception {
        String command = scanner.next();
        Vehicle.Type vehicleType = Vehicle.Type.valueOf(scanner.next().toUpperCase());

        if (Objects.equals(command, "free_count")) {
            parkingLotService.getCountOfEmptySlotsForVehicleType(vehicleType);
        } else if (Objects.equals(command, "occupied_slots")) {
            parkingLotService.getListOfFilledSlotsForVehicleType(vehicleType);
        } else if (Objects.equals(command, "free_slots")) {
            parkingLotService.getListOfEmptySlotsForVehicleType(vehicleType);
        }
    }

    private void handleParkVehicle(Scanner scanner, ParkingLotService parkingLotService) throws Exception {
        Vehicle.Type vehicleType = Vehicle.Type.valueOf(scanner.next().toUpperCase());

        String vehicleRegNo = scanner.next();
        Vehicle.Color color = Vehicle.Color.valueOf(scanner.next().toUpperCase());
        Vehicle vehicle = new Vehicle(vehicleRegNo, color, vehicleType);

        Ticket ticket = parkingLotService.getTicketForVehicleAndPark(vehicle);
        if (ticket == null) {
            log.info("Parking Lot full");
            return;
        }

        log.info("Parked vehicle. Ticket ID: {}", ticket.getTicketId());
    }

    private void handleUnParkVehicle(Scanner scanner, ParkingLotService parkingLotService) throws Exception {
        String ticketId = scanner.next();

        boolean wasUnparked =  parkingLotService.unparkVehicleWithTicketID(ticketId);
        if (!wasUnparked) {
            log.info("Invalid Ticket");
            return;
        }
    }

    public void runProgram() throws Exception {
        InputStream inputStream = Driver.class.getClassLoader().getResourceAsStream("input.txt");
        Scanner scanner = new Scanner(inputStream);

        ParkingLotService parkingLotService = null;
        while (scanner.hasNext()) {
            String nextCommand = scanner.next();

            log.debug("Command: {}", nextCommand);

            if (Objects.equals(nextCommand, "exit"))
                break;
            else if (Objects.equals(nextCommand, "create_parking_lot")) {
                String parkingLotID = scanner.next();
                Integer numFloors = scanner.nextInt();
                Integer numSlots = scanner.nextInt();

                log.info("Parking lot. id: {}, f: {}, s: {}", parkingLotID, numFloors, numSlots);
                List<Floor> floorList = new ArrayList<>();

                for (Integer i = 0; i < numFloors; i++) {
                    Integer floorId = i + 1;
                    // 1st slot = truck, 2nd and 3rd = bike, rest = car

                    List<Slot> slotList = new ArrayList<>();
                    for (Integer j = 0; j < numSlots; j++) {
                        Integer slotId = j + 1;
                        if (j == 0)
                            slotList.add(new Slot(slotId, Slot.Size.LARGE));
                        else if (j < 3)
                            slotList.add(new Slot(slotId, Slot.Size.COMPACT));
                        else
                            slotList.add(new Slot(slotId, Slot.Size.NORMAL));
                    }

                    floorList.add(new Floor(floorId, slotList));
                }

                ParkingLot parkingLot = new ParkingLot(parkingLotID, floorList);
                parkingLotService = new ParkingLotService(parkingLot, new ExactMatchSlotVehicleMappingStrategy());
            } else if (Objects.equals(nextCommand, "display")) {
                handleDisplay(scanner, parkingLotService);
            } else if (Objects.equals(nextCommand, "park_vehicle")) {
                handleParkVehicle(scanner, parkingLotService);
            } else if (Objects.equals(nextCommand, "unpark_vehicle")) {
                handleUnParkVehicle(scanner, parkingLotService);
            }
        }


    }
}
