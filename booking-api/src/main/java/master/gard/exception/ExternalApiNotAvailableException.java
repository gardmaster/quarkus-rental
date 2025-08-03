package master.gard.exception;

public class ExternalApiNotAvailableException extends RuntimeException {
    public ExternalApiNotAvailableException(String message) {
        super(message);
    }
}
