package master.gard.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/v1/vehicles")
@RegisterRestClient(configKey = "vehicle-api")
public interface VehicleApiClient {

    @GET
    @Path("/{vehicleId}")
    Vehicle findVehicleById(@PathParam("vehicleId") Long id);

    record Vehicle(String status) {
    }
}
