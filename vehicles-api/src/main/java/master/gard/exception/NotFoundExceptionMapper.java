package master.gard.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.dto.response.ErrorResponse;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException e) {
        ErrorResponse error = new ErrorResponse(
                e.getClass(),
                Response.Status.NOT_FOUND,
                e.getMessage(),
                java.time.LocalDateTime.now()
        );

        return Response.status(error.getStatus())
                .entity(error)
                .build();
    }
}
