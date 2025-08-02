package master.gard.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import master.gard.model.enums.VehicleStatus;

public record UpdateVehicleRequest(
        @NotBlank String brand,
        @NotNull @Min(1994) Long year,
        @NotBlank String model,
        @NotBlank String engine,
        @NotNull VehicleStatus status) {
}
