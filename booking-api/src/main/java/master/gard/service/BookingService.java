package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import master.gard.repository.BookingRepository;

@ApplicationScoped
public class BookingService {

    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

}
