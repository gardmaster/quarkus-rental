package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.model.Booking;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {
}
