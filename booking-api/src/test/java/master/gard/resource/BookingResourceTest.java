package master.gard.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;

@QuarkusTest
class BookingResourceTest {

//    // MANTER COMENTADO ENQUANTO A OUTRA API ESTÁ PARADA PARA ECONOMIZAR RAM
//    // DESCOMENTAR QUANDO FOR SUBIR O CODIGO
//    // TIRAR DUVIDAS COM O PROFESSOR EM RELAÇÃO A CONSISTENCIA DOS TESTES
//    @Test
//    void shouldCreateBookingSuccessfully() {
//
//        LocalDate startDate = LocalDate.now();
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
//                .statusCode(201)
//                .log();
//    }
//
//    @Test
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
//                            "endDate": "%s",
//                            "customerName": "GARD.MASTER"
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