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
import master.gard.repository.BookingRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BookingService {

    private static final String VEHICLE_DOES_NOT_EXIST_IN_VEHICLE_API = "Vehicle does not exist in Vehicle API";
    private static final String VEHICLE_NOT_FOUND_IN_VEHICLE_API = "Vehicle not found in Vehicle API";

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

            if (request.startDate().isAfter(request.endDate())) {
                throw new BusinessRuleException("Start date cannot be after end date");
            }

            if(!isVehicleAvailable(request.vehicleId(), request.startDate(), request.endDate())) {
                throw new BusinessRuleException("Vehicle is not available for the selected period");
            }

            Booking booking = new Booking(request.vehicleId(), request.customerName(), request.startDate(), request.endDate());
            this.bookingRepository.persist(booking);

        } catch (ClientWebApplicationException e) {
            if (e.getResponse().getStatus() == 404) {
                throw new NotFoundException(VEHICLE_DOES_NOT_EXIST_IN_VEHICLE_API);
            }
        }

    }

    // Como eu mantenho consistência entre Booking e Vehicle na booking-api?
    // ChatGPT: guardar dados necessários dos veículos nas colunas do Booking ou talvez usar uma tabela de snapshot
    // Perguntar ao professor a melhor maneira de fazer isso
    // Aqui eu tento recuperar o veículo do Vehicle API
    // E quando não consigo, eu utiliza o ID que já existe em Bookings e escrevo uma msg de "erro" no carTitle
    public List<BookingResponse> findAll() {
        List<BookingResponse> bookingResponses = new ArrayList<>();
        List<Booking> bookings = this.bookingRepository.findAll().list();

        for (Booking booking : bookings) {
            BookingResponse response;

            try {

                VehicleApiClient.Vehicle vehicle = vehicleApiClient.findVehicleById(booking.getVehicleId());
                response = new BookingResponse(booking, vehicle.carTitle());

            } catch (Exception e) {

                response = new BookingResponse(booking, VEHICLE_NOT_FOUND_IN_VEHICLE_API);

            }

            bookingResponses.add(response);
        }

        return bookingResponses;
    }

    public BookingResponse findById(Long id) {
        Booking booking = getBookingById(id);

        try {

            VehicleApiClient.Vehicle vehicle = vehicleApiClient.findVehicleById(booking.getVehicleId());
            return new BookingResponse(booking, vehicle.carTitle());

        } catch (Exception e) {
            return new BookingResponse(booking, VEHICLE_NOT_FOUND_IN_VEHICLE_API);
        }

    }

    @Transactional
    public BookingResponse updateStatus(Long id, UpdateBookingStatusRequest request) {
        Booking booking = getBookingById(id);
        booking.setStatus(request.status());

        try {

            VehicleApiClient.Vehicle vehicle = vehicleApiClient.findVehicleById(booking.getVehicleId());
            return new BookingResponse(booking, vehicle.carTitle());

        } catch (Exception e) {
            return new BookingResponse(booking, VEHICLE_NOT_FOUND_IN_VEHICLE_API);
        }
    }

    private Booking getBookingById(Long id) {
        return bookingRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + id + " not found"));
    }

    private boolean isVehicleAvailable(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findBookingByStatusAndPeriod(vehicleId, startDate, endDate) == null;
    }

}
