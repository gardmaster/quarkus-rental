package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import master.gard.client.VehicleApiClient;
import master.gard.dto.request.CreateBookingRequest;
import master.gard.dto.request.UpdateBookingStatusRequest;
import master.gard.dto.response.BookingResponse;
import master.gard.exception.BusinessRuleException;
import master.gard.model.Booking;
import master.gard.model.enums.BookingStatus;
import master.gard.repository.BookingRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class BookingService {

    private static final String VEHICLE_DOES_NOT_EXIST_IN_VEHICLE_API = "Vehicle does not exist in Vehicle API";

    private final BookingRepository bookingRepository;
    private final VehicleApiClient vehicleApiClient;

    public BookingService(BookingRepository bookingRepository, @RestClient VehicleApiClient vehicleApiClient) {
        this.bookingRepository = bookingRepository;
        this.vehicleApiClient = vehicleApiClient;
    }

    @Transactional
    public void createBooking(CreateBookingRequest request) {

        try {
            VehicleApiClient.Vehicle vehicle = vehicleApiClient.findVehicleById(request.vehicleId());

            if (!vehicle.status().equals("AVAILABLE")) {
                throw new BusinessRuleException("Vehicle is not available");
            }

            if (request.startDate().isBefore(LocalDate.now()) || request.endDate().isBefore(LocalDate.now())) {
                throw new BusinessRuleException("Start date and/or end date must not be in the past");
            }

            if (request.startDate().isAfter(request.endDate())) {
                throw new BusinessRuleException("Start date cannot be after end date");
            }

            if (isOverlappingBooking(request.vehicleId(), request.startDate(), request.endDate())) {
                throw new BusinessRuleException("Vehicle is not available for the selected period");
            }

            Booking booking = new Booking(request.vehicleId(), request.startDate(), request.endDate());
            this.bookingRepository.persist(booking);

        } catch (ClientWebApplicationException e) {
            if (e.getResponse().getStatus() == 404) {
                throw new NotFoundException(VEHICLE_DOES_NOT_EXIST_IN_VEHICLE_API);
            }
        }

    }

    public List<BookingResponse> findAll() {
        List<Booking> bookings = bookingRepository.listAll();
        return bookings.stream()
                .map(BookingResponse::new)
                .toList();
    }

    public BookingResponse findById(Long id) {
        return new BookingResponse(getBookingById(id));
    }

    @Transactional
    public BookingResponse updateStatus(Long id, UpdateBookingStatusRequest request) {
        Booking booking = getBookingById(id);
        booking.setStatus(request.status());
        return new BookingResponse(booking);
    }

    private Booking getBookingById(Long id) {
        return bookingRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + id + " not found"));
    }

    private boolean isOverlappingBooking(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findBookingByStatusAndPeriod(vehicleId, startDate, endDate) != null;
    }

    @Transactional
    public void checkIn(Long id) {
        Booking booking = getBookingById(id);
        booking.checkIn();
    }

    @Transactional
    public void checkOut(Long id) {
        Booking booking = getBookingById(id);
        booking.checkOut();
    }

    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.cancelBooking();
    }
}
