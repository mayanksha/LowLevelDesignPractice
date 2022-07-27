package com.practice.lld.strategy;

import com.practice.lld.model.Slot;
import com.practice.lld.model.Vehicle;

import java.util.Collections;
import java.util.List;

public class ExactMatchSlotVehicleMappingStrategy extends SlotVehicleMappingStrategy {

    @Override
    public List<Slot.Size> getSlotSizesForVehicleType(Vehicle.Type type) throws Exception {
        switch (type) {
            case BIKE:
                return Collections.singletonList(Slot.Size.COMPACT);
            case CAR:
                return Collections.singletonList(Slot.Size.NORMAL);
            case TRUCK:
                return Collections.singletonList(Slot.Size.LARGE);
            default:
                throw new Exception("Invalid vehicle type provided");
        }

    }
}
