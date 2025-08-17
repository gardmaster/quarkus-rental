package master.gard.dto.response;

import master.gard.model.Booking;
import master.gard.model.enums.BookingStatus;

import java.time.LocalDate;
import java.util.UUID;

public record BookingResponse(Long id,
                              UUID customerId,
                              BookingStatus status,
                              Long vehicleId,
                              LocalDate startDate,
                              LocalDate endDate) {

    public BookingResponse(Booking booking) {
        this(booking.getId(), booking.getCustomerId(), booking.getStatus(), booking.getVehicleId(),
                booking.getStartDate(), booking.getEndDate());
    }
}
