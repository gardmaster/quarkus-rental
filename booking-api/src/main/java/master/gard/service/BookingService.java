package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import master.gard.client.VehicleApiClient;
import master.gard.dto.request.CreateBookingRequest;
import master.gard.exception.BusinessRuleException;
import master.gard.model.Booking;
import master.gard.repository.BookingRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

@ApplicationScoped
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleApiClient vehicleApiClient;

    public BookingService(BookingRepository bookingRepository, @RestClient VehicleApiClient vehicleApiClient) {
        this.bookingRepository = bookingRepository;
        this.vehicleApiClient = vehicleApiClient;
    }

    @Transactional
    public void createBooking(CreateBookingRequest request) {

        try{
            VehicleApiClient.Vehicle vehicle = vehicleApiClient.findVehicleById(request.vehicleId());

            if (!vehicle.status().equals("AVAILABLE")) {
                throw new BusinessRuleException("Vehicle is not available");
            }

            if(request.startDate().isAfter(request.endDate())) {
                throw new BusinessRuleException("Start date cannot be after end date");
            }

            Booking booking = new Booking(request.vehicleId(), request.customerName(), request.startDate(), request.endDate());
            this.bookingRepository.persist(booking);

        }catch (ClientWebApplicationException e){

            if (e.getResponse().getStatus() == 404) {
                throw new NotFoundException("Vehicle does not exist in Vehicle API");

            } else {
                throw new BusinessRuleException("Vehicle service unavailable: " + e.getMessage());
            }

        }
    }

    private Booking getBookingById(Long id) {
        return this.bookingRepository.findById(id, LockModeType.PESSIMISTIC_WRITE);
    }

}
