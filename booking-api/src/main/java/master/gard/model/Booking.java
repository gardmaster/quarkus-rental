package master.gard.model;

import jakarta.persistence.*;
import lombok.Getter;
import master.gard.model.enums.BookingStatus;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Set;

@Getter
@Entity
@Table(name = "bookings")
public class Booking {

    private static final EnumMap<BookingStatus, Set<BookingStatus>> BOOKING_STATE_MACHINE
            = new EnumMap<>(BookingStatus.class);

    static {
        BOOKING_STATE_MACHINE.put(BookingStatus.CREATED, Set.of(BookingStatus.CANCELED, BookingStatus.FINISHED));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    protected Booking() {
    }

    public Booking(Long vehicleId, String customerName, LocalDate startDate, LocalDate endDate) {
        this.vehicleId = vehicleId;
        this.status = BookingStatus.CREATED;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
    }



    public void setStatus(BookingStatus incomingStatus) {

        if (incomingStatus == null) {
            throw new IllegalArgumentException("Incoming status cannot be null");
        }

        if (incomingStatus.equals(this.status)) {
            throw new IllegalArgumentException("Booking is already in status " + this.status);
        }

        Set<BookingStatus> validNextStatuses = BOOKING_STATE_MACHINE.get(this.status);

        if (validNextStatuses == null || !validNextStatuses.contains(incomingStatus)) {
            throw new IllegalArgumentException("Invalid status transition from " + this.status + " to " + incomingStatus);
        }

        this.status = incomingStatus;
    }

}
