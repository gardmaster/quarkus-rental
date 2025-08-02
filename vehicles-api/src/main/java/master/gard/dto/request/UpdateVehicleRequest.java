package master.gard.dto.request;

import master.gard.model.enums.VehicleStatus;

public record UpdateVehicleRequest(
        String brand,
        Long year,
        String model,
        String engine,
        VehicleStatus status) {
}
