package master.gard.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.dto.response.ErrorResponse;

@Provider
public class ExternalApiNotAvailableExceptionMapper implements ExceptionMapper<ExternalApiNotAvailableException> {

    @Override
    public Response toResponse(ExternalApiNotAvailableException e) {

        ErrorResponse error = new ErrorResponse(
                e.getClass(),
                Response.Status.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                java.time.LocalDateTime.now()
        );

        return Response.status(error.getStatus())
                .entity(error)
                .build();
    }

}
