package dev.cdy.studio.api.exceptions;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

public class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String errorCode, String message) {
        super(errorCode, message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(Class<?> clazz, Object o) {
        super("ENTITY_NOT_FOUND",
                MessageFormat.format("{0} [{1}] not found.", clazz.getSimpleName(), o),
                HttpStatus.NOT_FOUND);
    }
}