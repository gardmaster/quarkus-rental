package master.gard.dto.request;

public record CreateVehicleRequest(
        String brand,
        Long year,
        String model,
        String engine) {
}
