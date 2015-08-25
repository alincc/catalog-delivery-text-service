package no.nb.microservices.catalogdeliverytext.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Alto not found")
public class AltoNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AltoNotFoundException(String message) {
        super(message);
    }
}
