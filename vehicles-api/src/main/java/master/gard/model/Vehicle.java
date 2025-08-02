package master.gard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import master.gard.exception.BusinessRuleException;
import master.gard.exception.InvalidStatusTransitionException;
import master.gard.model.enums.VehicleStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Entity
@Table(name = "vehicles")
public class Vehicle {

    private static final Map<VehicleStatus, Set<VehicleStatus>> VEHICLE_STATE_MACHINE = new HashMap<>() {
    };

    static {
        VEHICLE_STATE_MACHINE.put(VehicleStatus.AVAILABLE, Set.of(VehicleStatus.RENTED, VehicleStatus.UNDER_MAINTENANCE));
        VEHICLE_STATE_MACHINE.put(VehicleStatus.RENTED, Set.of(VehicleStatus.AVAILABLE, VehicleStatus.UNDER_MAINTENANCE));
        VEHICLE_STATE_MACHINE.put(VehicleStatus.UNDER_MAINTENANCE, Set.of(VehicleStatus.AVAILABLE));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "brand")
    private String brand;

    @Setter
    @Column(name = "manufacture_year")
    private Long year;

    @Setter
    @Column(name = "model")
    private String model;

    @Setter
    @Column(name = "engine")
    private String engine;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VehicleStatus status;

    protected Vehicle() {
    }

    public Vehicle(String brand, Long year, String model, String engine) {
        this.brand = brand;
        this.year = year;
        this.model = model;
        this.engine = engine;
        this.status = VehicleStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return this.status == VehicleStatus.AVAILABLE;
    }

    public boolean isRented() {
        return this.status == VehicleStatus.RENTED;
    }

    public boolean isUnderMaintenance() {
        return this.status == VehicleStatus.UNDER_MAINTENANCE;
    }

    public void setStatus(VehicleStatus incomingStatus) {

        if (incomingStatus == null) {
            throw new BusinessRuleException("Incoming status cannot be null");
        }

        if (incomingStatus.equals(this.status)) {
            return;
        }

        Set<VehicleStatus> possibleStatus = VEHICLE_STATE_MACHINE.get(this.status);

        if (possibleStatus == null || !possibleStatus.contains(incomingStatus)) {
            throw new InvalidStatusTransitionException("Invalid status transition from " + this.status + " to " + incomingStatus);
        }

        this.status = incomingStatus;
    }
}