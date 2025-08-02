package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import master.gard.dto.request.CreateVehicleRequest;
import master.gard.dto.request.UpdateVehicleRequest;
import master.gard.dto.response.VehicleResponse;
import master.gard.exception.BusinessException;
import master.gard.model.Vehicle;
import master.gard.model.enums.VehicleStatus;
import master.gard.repository.VehicleRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<VehicleResponse> listAll() {
        return vehicleRepository.listAll().stream()
                .map(VehicleResponse::new)
                .toList();
    }

    @Transactional
    public void create(CreateVehicleRequest request) {
        Vehicle vehicle = new Vehicle(request.brand(), request.year(), request.model(), request.engine());
        vehicleRepository.persist(vehicle);
    }

    public VehicleResponse findById(Long id) {
        Vehicle vehicle = findByIdOrThrow(id);
        return new VehicleResponse(vehicle);
    }

    @Transactional
    public void delete(Long id) {
        Vehicle vehicle = findByIdOrThrow(id);
        this.deleteIfNotRented(vehicle);
    }

    @Transactional
    public VehicleResponse updateStatus(Long id, VehicleStatus status) {
        Vehicle vehicle = findByIdOrThrow(id);
        vehicle.setStatus(status);
        vehicleRepository.persist(vehicle);
        return new VehicleResponse(vehicle);
    }

    @Transactional
    public VehicleResponse update(Long id, UpdateVehicleRequest request) {
        Vehicle vehicle = findByIdOrThrow(id);
        vehicle.setBrand(request.brand());
        vehicle.setYear(request.year());
        vehicle.setModel(request.model());
        vehicle.setEngine(request.engine());
        vehicle.setStatus(request.status());
        vehicleRepository.persist(vehicle);
        return new VehicleResponse(vehicle);
    }

    public Vehicle findByIdOrThrow(Long id) {
        return Optional.ofNullable(vehicleRepository.findById(id))
                .orElseThrow(() -> new BusinessException("Vehicle not found with id: " + id));
    }

    public void deleteIfNotRented(Vehicle vehicle) {
        if (!vehicle.isRented()) {
            vehicleRepository.delete(vehicle);
        } else {
            throw new BusinessException("Vehicle cannot be deleted because it is RENTED");
        }
    }
}
