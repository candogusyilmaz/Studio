package cdy.studioapi.exceptions;

import cdy.studioapi.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    public NotFoundException(ExceptionCode ex, String message) {
        super(ex.name(), message, HttpStatus.NOT_FOUND);
    }
}