package master.gard.dto.request;

import master.gard.model.enums.VehicleStatus;

public record UpdateVehicleStatusRequest(VehicleStatus status) {
}
