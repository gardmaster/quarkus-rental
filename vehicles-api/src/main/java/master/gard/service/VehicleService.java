package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import master.gard.repository.VehicleRepository;

@ApplicationScoped
public class VehicleService {

    private VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

}
