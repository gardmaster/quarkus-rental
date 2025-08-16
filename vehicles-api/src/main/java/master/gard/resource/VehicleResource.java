package master.gard.resource;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.CreateVehicleRequest;
import master.gard.dto.request.UpdateVehicleRequest;
import master.gard.dto.request.UpdateVehicleStatusRequest;
import master.gard.dto.response.GenericPagedResponse;
import master.gard.dto.response.VehicleResponse;
import master.gard.service.VehicleService;

import java.util.List;

@Path(VehicleResource.API_V1_VEHICLES)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleResource {

    public static final String API_V1_VEHICLES = "/api/v1/vehicles/";

    private final VehicleService vehicleService;

    public VehicleResource(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GET
    public Response findAll() {
        List<VehicleResponse> vehicles = vehicleService.listAll();
        return vehicles.isEmpty() ?
                Response.status(Response.Status.NO_CONTENT).build() :
                Response.ok(vehicles).build();
    }

    @GET
    @Path("/pageable")
    public Response findAllPageable(@QueryParam("page") @DefaultValue("0") int page,
                                    @QueryParam("size") @DefaultValue("3") int size) {
        GenericPagedResponse<VehicleResponse> vehiclesPageable = vehicleService.listaAllPageable(page, size);
        return vehiclesPageable == null ?
                Response.status(Response.Status.NO_CONTENT).build() :
                Response.ok(vehiclesPageable).build();
    }

    @POST
    public Response create(@Valid CreateVehicleRequest request) {
        vehicleService.create(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        VehicleResponse vehicleResponse = vehicleService.findById(id);
        return Response.ok(vehicleResponse).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        vehicleService.delete(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}")
    public Response updateStatus(@PathParam("id") Long id, @Valid UpdateVehicleStatusRequest request) {
        VehicleResponse vehicleResponse = vehicleService.updateStatus(id, request.status());
        return Response.ok(vehicleResponse).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateVehicle(@PathParam("id") Long id, @Valid UpdateVehicleRequest request) {
        VehicleResponse vehicle = vehicleService.update(id, request);
        return Response.ok(vehicle).build();
    }
}
