package com.practice.lld.strategy;

import com.practice.lld.model.Slot;
import com.practice.lld.model.Vehicle;

import java.util.List;

public abstract class SlotVehicleMappingStrategy {
    public abstract List<Slot.Size> getSlotSizesForVehicleType(Vehicle.Type vehicle) throws Exception;
}
