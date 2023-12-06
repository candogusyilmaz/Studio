package dev.canverse.studio.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Object value) {
        super(message);
        this.body.setProperty("value", value);
    }

    @Override
    protected HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
