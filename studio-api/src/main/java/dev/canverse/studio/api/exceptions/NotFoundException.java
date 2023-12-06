package dev.canverse.studio.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Object entityKey) {
        super(message);
        this.body.setProperty("id", entityKey);
    }

    @Override
    protected HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}