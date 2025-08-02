package master.gard.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import master.gard.dto.request.CreateVehicleRequest;
import master.gard.dto.request.UpdateVehicleRequest;
import master.gard.dto.response.GenericPagedResponse;
import master.gard.dto.response.VehicleResponse;
import master.gard.exception.BusinessRuleException;
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

    // Paginação similar a que fiz no Spring do EmbarqueTI
    // Perguntar ao professor a melhor maneira de fazer isso no Quarkus
    public GenericPagedResponse<VehicleResponse> listaAllPageable(int page, int size) {
        PanacheQuery<Vehicle> query = vehicleRepository.findAll();

        long totalElements = query.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        if(page < 0 || size <= 0 || page >= totalPages) {
            throw new BusinessRuleException(("Page %d does not exist. Total pages: %d. " +
                    "Remember that page starts at 0").formatted(page, totalPages));
        }

        query.page(page, size);

        List<VehicleResponse> list = query.list().stream()
                .map(VehicleResponse::new)
                .toList();

        return GenericPagedResponse.of(
                list,
                totalElements,
                page,
                totalPages,
                size
        );

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

    private Vehicle findByIdOrThrow(Long id) {
        return Optional.ofNullable(vehicleRepository.findById(id))
                .orElseThrow(() -> new NotFoundException("Vehicle not found with id: " + id));
    }

    private void deleteIfNotRented(Vehicle vehicle) {
        if (!vehicle.isRented()) {
            vehicleRepository.delete(vehicle);
        } else {
            throw new BusinessRuleException("Vehicle cannot be deleted because it is RENTED");
        }
    }
}
