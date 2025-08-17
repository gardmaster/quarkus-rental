package master.gard.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record CreateBookingRequest(@NotNull @Positive Long vehicleId,
                                   @NotNull @FutureOrPresent LocalDate startDate,
                                   @NotNull @FutureOrPresent LocalDate endDate) {
}
