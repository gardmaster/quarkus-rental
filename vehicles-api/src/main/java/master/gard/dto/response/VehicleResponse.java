package master.gard.dto.response;

import master.gard.model.Vehicle;
import master.gard.model.enums.VehicleStatus;

public record VehicleResponse(Long id, String brand, String model, Long year, String engine, VehicleStatus status,
                              String carTitle) {

    public VehicleResponse(Vehicle vehicle) {
        this(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(), vehicle.getEngine(), vehicle.getStatus(),
                "%s %s %s".formatted(vehicle.getBrand(), vehicle.getModel(), vehicle.getEngine()));
    }

}
