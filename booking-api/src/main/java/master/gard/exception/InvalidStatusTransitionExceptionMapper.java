package master.gard.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.dto.response.ErrorResponse;

import java.time.LocalDateTime;

@Provider
public class InvalidStatusTransitionExceptionMapper implements ExceptionMapper<InvalidStatusTransitionException> {

    @Override
    public Response toResponse(InvalidStatusTransitionException e) {

        ErrorResponse error = new ErrorResponse(
                e.getClass(),
                Response.Status.CONFLICT,
                e.getMessage(),
                LocalDateTime.now()
        );

        return Response.status(error.getStatus())
                .entity(error)
                .build();

    }

}
