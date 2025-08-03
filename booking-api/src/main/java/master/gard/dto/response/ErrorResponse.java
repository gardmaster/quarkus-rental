package master.gard.dto.response;

import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private Class<?> exceptionClass;
    private Response.Status status;
    private String message;
    private LocalDateTime thrownAt;

}
