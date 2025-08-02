package master.gard.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.dto.response.ErrorResponse;

@Provider
public class BusinessRuleExceptionMapper implements ExceptionMapper<BusinessRuleException> {

    @Override
    public Response toResponse(BusinessRuleException e) {

        ErrorResponse error = new ErrorResponse(
                e.getClass(),
                Response.Status.BAD_REQUEST,
                e.getMessage(),
                java.time.LocalDateTime.now()
        );

        return Response.status(error.getStatus())
                .entity(error)
                .build();
    }

}
