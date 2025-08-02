package master.gard.dto.request;

import jakarta.validation.constraints.NotNull;
import master.gard.model.enums.VehicleStatus;

public record UpdateVehicleStatusRequest(@NotNull VehicleStatus status) {
}
