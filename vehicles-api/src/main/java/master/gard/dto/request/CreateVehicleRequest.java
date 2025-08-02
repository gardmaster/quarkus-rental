package master.gard.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateVehicleRequest(
        @NotBlank String brand,
        @NotNull @Min(1994) Long year,
        @NotBlank String model,
        @NotBlank String engine) {
}
