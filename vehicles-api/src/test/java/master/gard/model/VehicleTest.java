package master.gard.model;

import master.gard.exception.BusinessRuleException;
import master.gard.model.enums.VehicleStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    void shouldCreateVehicleSucessfully() {
        Vehicle vehicle = new Vehicle("Toyota", 2020L, "Corolla", "1.8L");
        assertNotNull(vehicle);
        assertEquals("Toyota", vehicle.getBrand());
        assertEquals(2020L, vehicle.getYear());
        assertEquals("Corolla", vehicle.getModel());
        assertEquals("1.8L", vehicle.getEngine());
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void shouldSetStatusToAvailable() {
        Vehicle vehicle = new Vehicle("Mercedes", 2021L, "C-Class", "2.0L");
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void shouldSetStatusToUnderMaintenance() {
        Vehicle vehicle = new Vehicle("Audi", 2019L, "A4", "2.0L");
        vehicle.setStatus(VehicleStatus.UNDER_MAINTENANCE);
        assertEquals(VehicleStatus.UNDER_MAINTENANCE, vehicle.getStatus());
    }

    @Test
    void shouldReturnTrueWhenStatusIsAvailable() {
        Vehicle vehicle = new Vehicle("Honda", 2023L, "Civic", "2.0L");
        assertTrue(vehicle.isAvailable());
    }

    @Test
    void shouldReturnTrueWhenStatusIsUnderMaintenance() {
        Vehicle vehicle = new Vehicle("Volkswagen", 2020L, "Golf", "1.4L");
        vehicle.setStatus(VehicleStatus.UNDER_MAINTENANCE);
        assertTrue(vehicle.isUnderMaintenance());
    }

    @Test
    void shouldReturnFalseWhenStatusIsNotUnderMaintenance() {
        Vehicle vehicle = new Vehicle("Subaru", 2019L, "Outback", "2.5L");
        assertFalse(vehicle.isUnderMaintenance());
    }

    @Test
    void shouldThrowExceptionWhenSettingStatusToNull() {
        Vehicle vehicle = new Vehicle("Kia", 2021L, "Sorento", "2.5L");

        Exception exception = assertThrows(BusinessRuleException.class, () ->
                vehicle.setStatus(null)
        );

        String expectedMessage = "Incoming status cannot be null";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldDoNothingWhenSettingSameStatus() {
        Vehicle vehicle = new Vehicle("Hyundai", 2022L, "Tucson", "2.0L");
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        assertDoesNotThrow(() -> vehicle.setStatus(VehicleStatus.AVAILABLE));
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void shouldDoNothingWhenUsingProtectedConstructor() {
        Vehicle vehicle = new Vehicle();
        assertNotNull(vehicle);
    }
}