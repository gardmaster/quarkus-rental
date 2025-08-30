package master.gard.model;

import jakarta.persistence.*;
import lombok.Getter;
import master.gard.exception.BusinessRuleException;
import master.gard.exception.InvalidStatusTransitionException;
import master.gard.model.enums.BookingStatus;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "bookings")
public class Booking {

    private static final EnumMap<BookingStatus, Set<BookingStatus>> BOOKING_STATE_MACHINE
            = new EnumMap<>(BookingStatus.class);

    static {
        BOOKING_STATE_MACHINE.put(BookingStatus.CREATED, Set.of(BookingStatus.ACTIVE, BookingStatus.CANCELED, BookingStatus.FINISHED));
        BOOKING_STATE_MACHINE.put(BookingStatus.ACTIVE, Set.of(BookingStatus.CANCELED, BookingStatus.FINISHED));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "customer_ID")
    private UUID customerId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @Column(name = "actived_at", updatable = false)
    private LocalDate activedAt;

    @Column(name = "canceled_at")
    private LocalDate canceledAt;

    @Column(name = "finished_at")
    private LocalDate finishedAt;

    protected Booking() {
    }

    public Booking(Long vehicleId, LocalDate startDate, LocalDate endDate, UUID customerId) {
        this.vehicleId = vehicleId;
        this.status = BookingStatus.CREATED;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public void setStatus(BookingStatus incomingStatus) {

        if (incomingStatus == null) {
            throw new BusinessRuleException("Incoming status cannot be null");
        }

        if (incomingStatus.equals(this.status)) {
            throw new InvalidStatusTransitionException("Booking is already in status " + this.status);
        }

        Set<BookingStatus> validNextStatuses = BOOKING_STATE_MACHINE.get(this.status);

        if (validNextStatuses == null || !validNextStatuses.contains(incomingStatus)) {
            throw new InvalidStatusTransitionException("Invalid status transition from " + this.status + " to " + incomingStatus);
        }

        this.status = incomingStatus;
    }

    public void checkIn() {
        this.setStatus(BookingStatus.ACTIVE);
        this.activedAt = LocalDate.now();
    }

    public void checkOut() {
        this.setStatus(BookingStatus.FINISHED);
        this.finishedAt = LocalDate.now();
    }

    public void cancelBooking() {
        this.setStatus(BookingStatus.CANCELED);
        this.canceledAt = LocalDate.now();
    }
}
