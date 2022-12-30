package cdy.studioapi.exceptions;

import cdy.studioapi.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {

    public BadRequestException(ExceptionCode ex, String message) {
        super(ex.name(), message, HttpStatus.BAD_REQUEST);
    }
}
