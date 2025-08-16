package master.gard.service;

import jakarta.ws.rs.NotFoundException;
import master.gard.client.VehicleApiClient;
import master.gard.dto.request.CreateBookingRequest;
import master.gard.dto.request.UpdateBookingStatusRequest;
import master.gard.dto.response.BookingResponse;
import master.gard.exception.BusinessRuleException;
import master.gard.exception.InvalidStatusTransitionException;
import master.gard.model.Booking;
import master.gard.model.enums.BookingStatus;
import master.gard.repository.BookingRepository;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static master.gard.model.enums.BookingStatus.CANCELED;
import static master.gard.model.enums.BookingStatus.CREATED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private static final String GIOVANNI_DUARTE = "Giovanni Duarte";
    private static final String BRASILIA_AMARELA = "Brasilia amarela";
    private static final String AVAILABLE = "AVAILABLE";
    private static final String RENTED = "RENTED";
    private static final String VEHICLE_IS_NOT_AVAILABLE = "Vehicle is not available";
    private static final String START_DATE_CANNOT_BE_AFTER_END_DATE = "Start date cannot be after end date";
    private static final String AYRTON_SENNA = "Ayrton Senna";
    private static final String NOT_AVAILABLE_FOR_THE_SELECTED_PERIOD = "Vehicle is not available for the selected period";
    private static final String NOT_EXIST_IN_VEHICLE_API = "Vehicle does not exist in Vehicle API";
    private static final String FERRARI_F_1 = "Ferrari F1";
    public static final String NOT_FOUND_IN_VEHICLE_API = "Vehicle not found in Vehicle API";

    @Mock
    private BookingRepository bookingRepositoryMock;

    @Mock
    private VehicleApiClient vehicleApiClientMock;

    @InjectMocks
    BookingService bookingServiceInjectedMock;

    private CreateBookingRequest request;
    private VehicleApiClient.Vehicle brasilia;
    private Booking bookedByGiovanni;


    @BeforeEach
    void setUp() {
        request = new CreateBookingRequest(1L, GIOVANNI_DUARTE,
                LocalDate.now(), LocalDate.now().plusDays(2));

        brasilia = new VehicleApiClient.Vehicle(BRASILIA_AMARELA, AVAILABLE);

        bookedByGiovanni = new Booking(1L, GIOVANNI_DUARTE, LocalDate.now(), LocalDate.now().plusDays(2));
    }

    @Test
    void shouldCreateBookingSuccessfully() {
        when(vehicleApiClientMock.findVehicleById(anyLong())).thenReturn(brasilia);
        doNothing().when(bookingRepositoryMock).persist(any(Booking.class));

        bookingServiceInjectedMock.createBooking(request);

        verify(vehicleApiClientMock).findVehicleById(1L);
        verify(bookingRepositoryMock).persist(any(Booking.class));
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsInThePast() {
        request = new CreateBookingRequest(1L, GIOVANNI_DUARTE,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(2));

        when(vehicleApiClientMock.findVehicleById(anyLong())).thenReturn(brasilia);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
                bookingServiceInjectedMock.createBooking(request)
        );

        assertEquals("Start date and/or end date must not be in the past", exception.getMessage());
        verify(vehicleApiClientMock).findVehicleById(1L);
        verify(bookingRepositoryMock, never()).persist(any(Booking.class));
    }

    @Test
    void shouldThrowExceptionWhenVehicleIsNotAvailable() {
        brasilia = new VehicleApiClient.Vehicle(BRASILIA_AMARELA, RENTED);

        when(vehicleApiClientMock.findVehicleById(anyLong())).thenReturn(brasilia);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
                bookingServiceInjectedMock.createBooking(request)
        );

        assertEquals(VEHICLE_IS_NOT_AVAILABLE, exception.getMessage());
        verify(vehicleApiClientMock).findVehicleById(1L);
        verify(bookingRepositoryMock, never()).persist(any(Booking.class));
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
        request = new CreateBookingRequest(1L, GIOVANNI_DUARTE,
                LocalDate.now().plusDays(2), LocalDate.now());

        when(vehicleApiClientMock.findVehicleById(anyLong())).thenReturn(brasilia);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
                bookingServiceInjectedMock.createBooking(request)
        );

        assertEquals(START_DATE_CANNOT_BE_AFTER_END_DATE, exception.getMessage());
        verify(vehicleApiClientMock).findVehicleById(1L);
        verify(bookingRepositoryMock, never()).persist(any(Booking.class));
    }

    @Test
    void shouldThrowExceptionWhenVehicleIsNotAvailableForSelectedPeriod() {
        Booking booking = new Booking(2L, AYRTON_SENNA, LocalDate.now(), LocalDate.now().plusDays(1));

        when(vehicleApiClientMock.findVehicleById(anyLong())).thenReturn(brasilia);
        when(bookingRepositoryMock.findBookingByStatusAndPeriod(1L, request.startDate(), request.endDate()))
                .thenReturn(booking);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
                bookingServiceInjectedMock.createBooking(request)
        );

        assertEquals(NOT_AVAILABLE_FOR_THE_SELECTED_PERIOD, exception.getMessage());
        verify(vehicleApiClientMock).findVehicleById(1L);
        verify(bookingRepositoryMock).findBookingByStatusAndPeriod(1L, request.startDate(), request.endDate());
        verify(bookingRepositoryMock, never()).persist(any(Booking.class));
    }

    @Test
    void shouldThrowExceptionWhenVehicleDoesNotExistInVehicleApi() {
        when(vehicleApiClientMock.findVehicleById(anyLong()))
                .thenThrow(new ClientWebApplicationException(NOT_EXIST_IN_VEHICLE_API, 404));

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                bookingServiceInjectedMock.createBooking(request)
        );

        assertEquals(NOT_EXIST_IN_VEHICLE_API, exception.getMessage());
        verify(vehicleApiClientMock).findVehicleById(1L);
        verify(bookingRepositoryMock, never()).persist(any(Booking.class));
    }

    @Test
    void shouldReturnListOfBookingsSuccessfully() {
        List<Booking> bookings = getBookings();

        when(bookingRepositoryMock.listAll()).thenReturn(bookings);
        when(vehicleApiClientMock.findVehicleById(anyLong()))
                .thenReturn(new VehicleApiClient.Vehicle(BRASILIA_AMARELA, AVAILABLE))
                .thenReturn(new VehicleApiClient.Vehicle(FERRARI_F_1, AVAILABLE));

        List<BookingResponse> actualBookings = bookingServiceInjectedMock.findAll();

        assertNotNull(actualBookings);
        assertEquals(2, actualBookings.size());
        assertEquals(GIOVANNI_DUARTE, actualBookings.get(0).customerName());
        assertEquals(AYRTON_SENNA, actualBookings.get(1).customerName());
    }

    @Test
    void shouldReturnListOfBookingsWithoutCarTitlesWhenVehiclesNotFound() {
        List<Booking> bookings = getBookings();

        when(bookingRepositoryMock.listAll()).thenReturn(bookings);
        when(vehicleApiClientMock.findVehicleById(anyLong()))
                .thenThrow(new ClientWebApplicationException(NOT_FOUND_IN_VEHICLE_API, 404));

        List<BookingResponse> actualBookings = bookingServiceInjectedMock.findAll();

        assertNotNull(actualBookings);
        assertEquals(2, actualBookings.size());
        assertEquals(GIOVANNI_DUARTE, actualBookings.get(0).customerName());
        assertEquals(AYRTON_SENNA, actualBookings.get(1).customerName());
    }

    @Test
    void shouldReturnBookingByIdSuccessfully() {
        when(bookingRepositoryMock.findByIdOptional(anyLong())).thenReturn(Optional.of(bookedByGiovanni));
        when(vehicleApiClientMock.findVehicleById(anyLong()))
                .thenReturn(new VehicleApiClient.Vehicle(BRASILIA_AMARELA, AVAILABLE));

        BookingResponse actualBooking = bookingServiceInjectedMock.findById(1L);
        assertNotNull(actualBooking);
        assertEquals(GIOVANNI_DUARTE, actualBooking.customerName());
    }

    @Test
    void shouldReturnBookingByIdWithoutCarTitleWhenVehicleNotFound() {
        when(bookingRepositoryMock.findByIdOptional(anyLong())).thenReturn(Optional.of(bookedByGiovanni));
        when(vehicleApiClientMock.findVehicleById(anyLong()))
                .thenThrow(new ClientWebApplicationException(NOT_FOUND_IN_VEHICLE_API, 404));

        BookingResponse actualBooking = bookingServiceInjectedMock.findById(1L);
        assertNotNull(actualBooking);
        assertEquals(GIOVANNI_DUARTE, actualBooking.customerName());
    }

    @Test
    void shouldThrowExceptionWhenBookingNotFoundById() {
        when(bookingRepositoryMock.findByIdOptional(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                bookingServiceInjectedMock.findById(1L)
        );

        assertEquals("Booking with ID 1 not found", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(value = BookingStatus.class, names = {"CANCELED", "FINISHED"})
    void shouldUpdateBookingStatusSuccessfully(BookingStatus status) {
        when(bookingRepositoryMock.findByIdOptional(anyLong())).thenReturn(Optional.of(bookedByGiovanni));
        when(vehicleApiClientMock.findVehicleById(anyLong()))
                .thenReturn(new VehicleApiClient.Vehicle(BRASILIA_AMARELA, AVAILABLE));

        BookingResponse updatedBooking = bookingServiceInjectedMock.updateStatus(1L, new UpdateBookingStatusRequest(status));

        assertNotNull(updatedBooking);
        assertEquals(GIOVANNI_DUARTE, updatedBooking.customerName());
        assertEquals(status, updatedBooking.status());
    }

    @ParameterizedTest
    @EnumSource(value = BookingStatus.class, names = {"CANCELED", "FINISHED"})
    void shouldThrowExceptionWhenUpdatingBookingStatusToTheSame(BookingStatus status) {
        bookedByGiovanni.setStatus(status);

        UpdateBookingStatusRequest updateRequest = new UpdateBookingStatusRequest(status);

        when(bookingRepositoryMock.findByIdOptional(anyLong())).thenReturn(Optional.of(bookedByGiovanni));

        InvalidStatusTransitionException exception = assertThrows(InvalidStatusTransitionException.class, () ->
                bookingServiceInjectedMock.updateStatus(1L, updateRequest)
        );

        assertEquals("Booking is already in status " + status, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingBookingToInvalidStatus() {
        bookedByGiovanni.setStatus(CANCELED);

        UpdateBookingStatusRequest updateRequest = new UpdateBookingStatusRequest(CREATED);

        when(bookingRepositoryMock.findByIdOptional(anyLong())).thenReturn(Optional.of(bookedByGiovanni));

        InvalidStatusTransitionException exception = assertThrows(InvalidStatusTransitionException.class, () ->
                bookingServiceInjectedMock.updateStatus(1L, updateRequest)
        );

        assertEquals("Invalid status transition from " + CANCELED + " to " + CREATED, exception.getMessage());
    }

    @Test
    void shouldReturnBookingWithoutCarTitleWhenVehicleNotFound() {
        when(bookingRepositoryMock.findByIdOptional(anyLong())).thenReturn(Optional.of(bookedByGiovanni));
        when(vehicleApiClientMock.findVehicleById(anyLong()))
                .thenThrow(new ClientWebApplicationException(NOT_FOUND_IN_VEHICLE_API, 404));

        BookingResponse actualBooking = bookingServiceInjectedMock.updateStatus(1L, new UpdateBookingStatusRequest(CANCELED));
        assertNotNull(actualBooking);
        assertEquals(GIOVANNI_DUARTE, actualBooking.customerName());
    }

    private static List<Booking> getBookings() {
        return List.of(
                new Booking(1L, GIOVANNI_DUARTE, LocalDate.now(), LocalDate.now().plusDays(2)),
                new Booking(2L, AYRTON_SENNA, LocalDate.now(), LocalDate.now().plusDays(1))
        );
    }
}