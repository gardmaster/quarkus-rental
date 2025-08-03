package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.model.Booking;

import java.time.LocalDate;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {

    public Booking findBookingByStatusAndPeriod(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        return find("vehicleId = ?1 and status = 'CREATED' and " +
                        "(" +
                        "startDate <= ?2 and endDate >= ?2 or " +
                        "startDate <= ?3 and endDate >= ?3 or " +
                        "startDate >= ?2 and endDate <= ?3 " +
                        ")",
                vehicleId, startDate, endDate)
                .firstResult();
    }

}
