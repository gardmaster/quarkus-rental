package master.gard.resource;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.CreateBookingRequest;
import master.gard.dto.request.UpdateBookingStatusRequest;
import master.gard.dto.response.BookingResponse;
import master.gard.service.BookingService;

import java.util.List;

@Path("api/v1/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookingResource {

    private final BookingService bookingService;

    public BookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GET
    public Response findAll() {
        List<BookingResponse> bookings = bookingService.findAll();
        return bookings.isEmpty() ?
                Response.status(Response.Status.NO_CONTENT).build() :
                Response.ok(bookings).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        BookingResponse bookingResponse = bookingService.findById(id);
        return Response.ok(bookingResponse).build();
    }

    @POST
    public Response createBooking(@Valid CreateBookingRequest request) {
        bookingService.createBooking(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @PATCH
    @Path("/{id}")
    public Response updateBookingStatus(@PathParam("id") Long id, @Valid UpdateBookingStatusRequest request) {
        BookingResponse response = bookingService.updateStatus(id, request);
        return Response.ok(response).build();
    }
}
