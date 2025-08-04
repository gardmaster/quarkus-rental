package master.gard.dto.request;

import jakarta.validation.constraints.NotNull;
import master.gard.model.enums.BookingStatus;

public record UpdateBookingStatusRequest(@NotNull BookingStatus status) {
}
