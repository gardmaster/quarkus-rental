package master.gard.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import master.gard.model.enums.VehicleStatus;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class VehicleResourceTest {

    @Test
    void shouldGetAllVehicles() {
        given()
                .get("/api/v1/vehicles")
                .then()
                .body("size()", Matchers.not(Matchers.empty()))
                .statusCode(HttpStatus.SC_OK)
                .body("[0].id", Matchers.notNullValue())
                .body("[0].carTitle", equalTo("Mercedes-Benz A-Class 2.0 Turbo"))
                .log();
    }

    @Test
    void shouldGetStatus409WhenUpdatingVehicleWithInvalidStatusTransition() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "status": "UNDER_MAINTENANCE"
                        }
                        """)
                .when()
                .patch(VehicleResource.API_V1_VEHICLES + "1")
                .then()
                .statusCode(200)
                .body("status", equalTo(VehicleStatus.UNDER_MAINTENANCE.toString()));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "status": "RENTED"
                        }
                        """)
                .when()
                .patch(VehicleResource.API_V1_VEHICLES + "1")
                .then()
                .statusCode(409)
                .body("message", containsString("Invalid status transition from UNDER_MAINTENANCE to RENTED"));

    }

    @Test
    void shouldGetStatus400WhenCreatingVehicleWithoutRequiredFields() {

        given().contentType(ContentType.JSON)
                .body("""
                        {
                        }
                        """)
                .when()
                .post(VehicleResource.API_V1_VEHICLES)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("title", equalTo("Constraint Violation"))
                .body("status", equalTo(400))
                .body("violations", hasSize(4))
                .body("violations.field", hasItems(
                        "create.request.year",
                        "create.request.brand",
                        "create.request.engine",
                        "create.request.model"
                ))
                .body("violations.message", hasItems(
                        "não deve ser nulo",
                        "não deve estar em branco"
                ));

    }

    @Test
    void shouldGetStatus404WhenVehicleNotFound() {
        given()
                .get(VehicleResource.API_V1_VEHICLES + "9999")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("status", equalTo("NOT_FOUND"))
                .body("message", equalTo("Vehicle not found with id: 9999"));
    }
}