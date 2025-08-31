package master.gard.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.AttributeType;
import io.quarkus.test.security.SecurityAttribute;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import master.gard.dto.request.UpdateBookingStatusRequest;
import master.gard.dto.response.BookingResponse;
import master.gard.model.enums.BookingStatus;
import master.gard.service.BookingService;
import master.gard.utils.SecurityUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@QuarkusTest
class BookingResourceIntegrationTest {

    @InjectMock
    private BookingService bookingService;

    @InjectMock
    private SecurityUtils securityUtils;

    @AfterEach
    void resetMocks() {
        Mockito.reset(bookingService, securityUtils);
    }

    @Test
    @TestSecurity(user = "GARD.MASTER",
            roles = {"admin"})
    void shouldGetStatus200WhenGettingAllBookings() {
        mockAsAdmin();
        BookingResponse bookingResponse = mock(BookingResponse.class);
        Mockito.when(bookingService.findAll()).thenReturn(List.of(bookingResponse));

        given()
                .contentType(ContentType.JSON)
                .get("/api/v1/bookings")
                .then()
                .statusCode(200)
                .body("size()", Matchers.not(Matchers.empty()))
                .log();

    }

    @Test
    @TestSecurity(user = "FAKE.MASTER",
            roles = {"user"})
    void shouldGetStatus403WhenGettingAllBookings() {

        BookingResponse bookingResponse = mock(BookingResponse.class);
        Mockito.when(bookingService.findAll()).thenReturn(List.of(bookingResponse));

        given()
                .contentType(ContentType.JSON)
                .get("/api/v1/bookings")
                .then()
                .statusCode(403)
                .log();
    }

    @Test
    @TestSecurity(user = "GARD.MASTER",
            roles = {"admin"})
    void shouldGetStatus204WhenGettingNoBooking() {
        mockAsAdmin();
        Mockito.when(bookingService.findAll()).thenReturn(new ArrayList<>());

        given().contentType(ContentType.JSON)
                .get("/api/v1/bookings")
                .then()
                .statusCode(204)
                .log();

    }


    @Test
    @TestSecurity(user = "GARD.MASTER",
            roles = {"admin"})
    void shouldGetStatus200WhenGettingBookingById() {
        mockAsAdmin();
        BookingResponse bookingResponse = new BookingResponse(1L, UUID.randomUUID(), BookingStatus.CREATED, 1L,
                null, null, null, null, null);
        Mockito.when(bookingService.findById(anyLong(), isNull())).thenReturn(bookingResponse);

        given()
                .contentType(ContentType.JSON)
                .get("/api/v1/bookings/1")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(1))
                .body("vehicleId", Matchers.equalTo(1))
                .log();

    }

    @Test
    @TestSecurity(user = "FAKE.MASTER",
            roles = {"user"},
            attributes = {@SecurityAttribute(key = "sub", value = "123e4567-e89b-12d3-a456-426614174000", type = AttributeType.STRING)})
    void shouldGetStatus200WhenGettingBookingByIdThatIOwn() {

        mockAsUser(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        BookingResponse bookingResponse = new BookingResponse(1L, UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                BookingStatus.CREATED, 1L,
                null, null, null, null, null);
        Mockito.when(bookingService.findById(1L, UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))).thenReturn(bookingResponse);

        given()
                .contentType(ContentType.JSON)
                .get("/api/v1/bookings/1")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(1))
                .body("vehicleId", Matchers.equalTo(1))
                .log();

    }

    @Test
    @TestSecurity(user = "GARD.MASTER", roles = {"admin"})
    void shouldReturn200WhenCheckInAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .post("/api/v1/bookings/checkIn/1")
                .then()
                .statusCode(200);

        verify(bookingService, times(1)).checkIn(1L);
    }

    @Test
    @TestSecurity(user = "GARD.MASTER", roles = {"admin"})
    void shouldReturn200WhenCheckOutAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .post("/api/v1/bookings/checkOut/1")
                .then()
                .statusCode(200);

        verify(bookingService, times(1)).checkOut(1L);
    }

    @Test
    @TestSecurity(user = "GARD.MASTER", roles = {"admin"})
    void shouldReturn200WhenCancelBookingAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .post("/api/v1/bookings/cancel/1")
                .then()
                .statusCode(200);

        verify(bookingService, times(1)).cancelBooking(1L);
    }

    @Test
    @TestSecurity(user = "GARD.MANAGER", roles = {"admin"})
    void shouldReturn200AndCallServiceWhenUpdateStatusAsAdmin() {
        BookingResponse resp = new BookingResponse(1L, UUID.randomUUID(), BookingStatus.ACTIVE, 1L,
                null, null, null, null, null);
        when(bookingService.updateStatus(eq(1L), any(UpdateBookingStatusRequest.class))).thenReturn(resp);

        String json = """
                {
                  "status": "ACTIVE"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .patch("/api/v1/bookings/1")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(1))
                .body("status", Matchers.equalTo("ACTIVE"));
    }

    private void mockAsAdmin() {
        Mockito.when(securityUtils.hasRole("admin")).thenReturn(true);
        Mockito.when(securityUtils.hasRole("employee")).thenReturn(false);
    }

    private void mockAsUser(UUID uuid) {
        Mockito.when(securityUtils.hasRole("admin")).thenReturn(false);
        Mockito.when(securityUtils.hasRole("employee")).thenReturn(false);
        Mockito.when(securityUtils.getUserUuid()).thenReturn(uuid.toString());
    }
}
