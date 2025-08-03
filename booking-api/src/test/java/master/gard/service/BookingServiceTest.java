package master.gard.service;

import master.gard.client.VehicleApiClient;
import master.gard.dto.request.CreateBookingRequest;
import master.gard.exception.BusinessRuleException;
import master.gard.model.Booking;
import master.gard.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepositoryMock;

    @Mock
    private VehicleApiClient vehicleApiClientMock;

    @InjectMocks
    BookingService bookingServiceInjectedMock;

    @Test
    void shouldCreateBookingSuccessfully() {
        CreateBookingRequest request = new CreateBookingRequest(1L, "Giovanni Duarte",
                LocalDate.now(), LocalDate.now().plusDays(2));

        VehicleApiClient.Vehicle vehicle = new VehicleApiClient.Vehicle("Brasilia amarela", "AVAILABLE");

        when(vehicleApiClientMock.findVehicleById(anyLong())).thenReturn(vehicle);
        doNothing().when(bookingRepositoryMock).persist(any(Booking.class));

        bookingServiceInjectedMock.createBooking(request);

        verify(vehicleApiClientMock).findVehicleById(1L);
        verify(bookingRepositoryMock).persist(any(Booking.class));
    }

    @Test
    void shouldThrowExceptionWhenVehicleIsNotAvailable() {
        CreateBookingRequest request = new CreateBookingRequest(1L, "Giovanni Duarte",
                LocalDate.now(), LocalDate.now().plusDays(2));

        VehicleApiClient.Vehicle vehicle = new VehicleApiClient.Vehicle("Brasilia amarela", "RENTED");

        when(vehicleApiClientMock.findVehicleById(anyLong())).thenReturn(vehicle);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
            bookingServiceInjectedMock.createBooking(request)
        );

        assertEquals("Vehicle is not available", exception.getMessage());
        verify(vehicleApiClientMock).findVehicleById(1L);
        verify(bookingRepositoryMock, never()).persist(any(Booking.class));
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
        CreateBookingRequest request = new CreateBookingRequest(1L, "Giovanni Duarte",
                LocalDate.now().plusDays(2), LocalDate.now());

        VehicleApiClient.Vehicle vehicle = new VehicleApiClient.Vehicle("Brasilia amarela", "AVAILABLE");

        when(vehicleApiClientMock.findVehicleById(anyLong())).thenReturn(vehicle);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
            bookingServiceInjectedMock.createBooking(request)
        );

        assertEquals("Start date cannot be after end date", exception.getMessage());
        verify(vehicleApiClientMock).findVehicleById(1L);
        verify(bookingRepositoryMock, never()).persist(any(Booking.class));
    }

}