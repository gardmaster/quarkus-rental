package master.gard.dto.response;

import master.gard.model.Booking;
import master.gard.model.enums.BookingStatus;

import java.time.LocalDate;

public record BookingResponse(Long id,
                              String customerName,
                              BookingStatus status,
                              Long vehicleId,
                              String carTitle,
                              LocalDate startDate,
                              LocalDate endDate) {

    public BookingResponse(Booking booking, String carTitle) {
        this(booking.getId(), booking.getCustomerName(), booking.getStatus(), booking.getVehicleId(), carTitle,
                booking.getStartDate(), booking.getEndDate());
    }
}
