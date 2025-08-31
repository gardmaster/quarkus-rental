package master.gard.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;
import master.gard.dto.request.UpdateVehicleRequest;
import master.gard.dto.response.GenericPagedResponse;
import master.gard.dto.response.VehicleResponse;
import master.gard.model.enums.VehicleStatus;
import master.gard.service.VehicleService;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@QuarkusTest
class VehicleResourceTest {

    @InjectMock
    private VehicleService vehicleService;

    @AfterEach
    void resetMocks() {
        reset(vehicleService);
    }

    @Test
    void shouldGetAllVehicles() {

        VehicleResponse vehicleResponse = new VehicleResponse(1L, "Mercedes-Benz",
                "A-Class", 2020L, "2.0 Turbo", VehicleStatus.AVAILABLE, "Mercedes-Benz A-Class 2.0 Turbo");

        when(vehicleService.listAll()).thenReturn(List.of(vehicleResponse));

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
    void shouldGet204WhenThereIsNoVehicle() {
        when(vehicleService.listAll()).thenReturn(new ArrayList<>());

        given()
                .get("/api/v1/vehicles")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .log();
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
        when(vehicleService.findById(anyLong())).thenThrow(NotFoundException.class);

        given()
                .get(VehicleResource.API_V1_VEHICLES + "9999")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("status", equalTo("NOT_FOUND"));
    }

    @Test
    void shouldGetStatus200WhenCreatingVehicle() {
        given().contentType(ContentType.JSON)
                .body("""
                        {
                            "brand": "Mercedes-Benz",
                            "model": "A-Class",
                            "year": 2020,
                            "engine": "2.0 Turbo"
                        }
                        """)
                .when()
                .post(VehicleResource.API_V1_VEHICLES)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    void shouldGet200WhenUpdatingVehicleStatus() {
        VehicleResponse vehicleResponse = new VehicleResponse(1L, "Mercedes-Benz",
                "A-Class", 2020L, "2.0 Turbo", VehicleStatus.UNDER_MAINTENANCE, "Mercedes-Benz A-Class 2.0 Turbo");

        when(vehicleService.updateStatus(anyLong(), eq(VehicleStatus.UNDER_MAINTENANCE))).thenReturn(vehicleResponse);

        given().contentType(ContentType.JSON)
                .body("""
                        {
                            "status": "UNDER_MAINTENANCE"
                        }
                        """)
                .when()
                .patch(VehicleResource.API_V1_VEHICLES + "1")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(1))
                .body("status", equalTo("UNDER_MAINTENANCE"))
                .log();
    }

    @Test
    void shouldGet200WhenFindingVehicleById() {
        VehicleResponse vehicleResponse = new VehicleResponse(1L, "Mercedes-Benz",
                "A-Class", 2020L, "2.0 Turbo", VehicleStatus.AVAILABLE, "Mercedes-Benz A-Class 2.0 Turbo");

        when(vehicleService.findById(anyLong())).thenReturn(vehicleResponse);

        given()
                .get(VehicleResource.API_V1_VEHICLES + "1")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(1))
                .body("carTitle", equalTo("Mercedes-Benz A-Class 2.0 Turbo"))
                .log();
    }

    @Test
    void shouldGet200WhenFindingVehiclesPageable() {
        VehicleResponse vehicleResponse = new VehicleResponse(1L, "Mercedes-Benz",
                "A-Class", 2020L, "2.0 Turbo", VehicleStatus.AVAILABLE, "Mercedes-Benz A-Class 2.0 Turbo");

        when(vehicleService.listaAllPageable(0, 3))
                .thenReturn(new GenericPagedResponse<>(
                        List.of(vehicleResponse),
                        1,
                        1,
                        0,
                        3
                ));

        given()
                .get(VehicleResource.API_V1_VEHICLES + "pageable?page=0&size=3")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("content.size()", is(1))
                .body("totalElements", is(1))
                .body("totalPages", is(1))
                .log();
    }

    @Test
    void shouldGet200WhenUpdatingVehicle() {
        VehicleResponse vehicleResponse = new VehicleResponse(1L, "Mercedes-Benz",
                "A-Class", 2020L, "2.0 Turbo", VehicleStatus.AVAILABLE, "Mercedes-Benz A-Class 2.0 Turbo");

        when(vehicleService.update(1L,
                new UpdateVehicleRequest("Mercedes-Benz", 2020L, "A-Class", "2.0 Turbo", VehicleStatus.AVAILABLE)))
                .thenReturn(vehicleResponse);

        given().contentType(ContentType.JSON)
                .body("""
                        {
                            "brand": "Mercedes-Benz",
                            "model": "A-Class",
                            "year": 2020,
                            "engine": "2.0 Turbo",
                            "status": "AVAILABLE"
                        }
                        """)
                .when()
                .put(VehicleResource.API_V1_VEHICLES + "1")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(1))
                .body("carTitle", equalTo("Mercedes-Benz A-Class 2.0 Turbo"))
                .log();
    }
}