package master.gard.resource;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.AttributeType;
import io.quarkus.test.security.SecurityAttribute;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import master.gard.client.VehicleApiClient;
import master.gard.service.BookingService;
import master.gard.utils.SecurityUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.Security;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.anyLong;

@QuarkusTest
class BookingResourceTest {

//    @Inject
//    private SecurityIdentity securityIdentity;
//
//    @InjectMock
//    @RestClient
//    private VehicleApiClient vehicleApiClient;
//
//    @InjectMock
//    private BookingService bookingService;
//
//    @InjectMock
//    private SecurityUtils securityUtils;
//
//    @InjectMock
//    private BookingResource bookingResource;
//
//    @Test
//    @TestSecurity(user = "GARD.MASTER",
//            roles = {"admin"},
//            attributes = { @SecurityAttribute(key = "sub", value = "123e4567-e89b-12d3-a456-426614174000", type = AttributeType.STRING) })
//    void shouldCreateBookingSuccessfully() {
//
//        LocalDate startDate = LocalDate.now();
//        LocalDate endDate = startDate.plusDays(3);
//
//        Mockito.when(securityUtils.getUserUuid()).thenReturn("123e4567-e89b-12d3-a456-426614174000");
//
//        Mockito.when(vehicleApiClient.findVehicleById(anyLong()))
//                .thenReturn(new VehicleApiClient.Vehicle("1", "AVAILABLE"));
//
//
//
//        given().contentType(ContentType.JSON)
//                .body("""
//                        {
//                            "vehicleId": 1,
//                            "startDate": "%s",
//                            "endDate": "%s"
//                        }
//                        """.formatted(startDate, endDate))
//                .when()
//                .post("/api/v1/bookings")
//                .then()
//                .statusCode(201)
//                .log();
//    }
//
//    @Test
//    @TestSecurity(user = "GARD.MASTER",
//            roles = {"admin"},
//            attributes = { @SecurityAttribute(key = "sub", value = "123e4567-e89b-12d3-a456-426614174000", type = AttributeType.STRING) })
//    void shouldGetStatus400WhenCreatingBookingWithEndDateBeforeStartDate() {
//
//        LocalDate startDate = LocalDate.now().plusDays(10);
//        LocalDate endDate = startDate.minusDays(1);
//
//        given().contentType(ContentType.JSON)
//                .body("""
//                        {
//                            "vehicleId": 1,
//                            "startDate": "%s",
//                            "endDate": "%s"
//                        }
//                        """.formatted(startDate, endDate))
//                .when()
//                .post("/api/v1/bookings")
//                .then()
//                .statusCode(400)
//                .body("message", Matchers.containsString("Start date cannot be after end date"))
//                .log();
//    }
//
//    @Test
//    void shouldGetStatus400WhenCreatingBookingWithPastStartDate() {
//
//        LocalDate startDate = LocalDate.now().minusDays(1);
//        LocalDate endDate = startDate.plusDays(3);
//
//        given().contentType(ContentType.JSON)
//                .body("""
//                        {
//                            "vehicleId": 1,
//                            "startDate": "%s",
//                            "endDate": "%s",
//                            "customerName": "GARD.MASTER"
//                        }
//                        """.formatted(startDate, endDate))
//                .when()
//                .post("/api/v1/bookings")
//                .then()
//                .statusCode(400)
//                .body("violations.message", Matchers.contains("deve ser uma data no presente ou no futuro"))
//                .log();
//    }
//
////    //Perguntar ao professor qual seria a melhor maneira de aplicar este teste
////    @Test
////    void shouldGetStatus204WhenThereIsNoBookingInDatabase() {
////
////        given()
////                .contentType(ContentType.JSON)
////                .get("/api/v1/bookings")
////                .then()
////                .statusCode(204)
////                .log();
////
////    }
//
//    @Test
//    void shouldGetStatus200WhenGettingAllBookings() {
//
//        given()
//                .contentType(ContentType.JSON)
//                .get("/api/v1/bookings")
//                .then()
//                .statusCode(200)
//                .body("size()", Matchers.not(Matchers.empty()))
//                .log();
//
//    }
//
//    @Test
//    void shouldGetStatus200WhenGettingBookingById() {
//
//        given()
//                .contentType(ContentType.JSON)
//                .get("/api/v1/bookings/1")
//                .then()
//                .statusCode(200)
//                .body("id", Matchers.equalTo(1))
//                .body("vehicleId", Matchers.equalTo(1))
//                .body("customerName", Matchers.equalTo("Bruce Wayne"))
//                .log();
//
//    }

}