package master.gard.dto.response;

import jakarta.ws.rs.core.Response.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private Class<?> exceptionClass;
    private Status status;
    private String message;
    private LocalDateTime thrownAt;

}
