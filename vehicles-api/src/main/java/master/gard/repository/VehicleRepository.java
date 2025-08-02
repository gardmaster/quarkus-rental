package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.model.Vehicle;

@ApplicationScoped
public class VehicleRepository implements PanacheRepository<Vehicle> {

}
