package dev.cdy.studio.api.exceptions;

import org.springframework.http.HttpStatus;

public abstract class DomainException extends RuntimeException {
    public final HttpStatus httpStatus;
    public final ErrorResponse errorResponse;

    protected DomainException(String message, HttpStatus status) {
        this.httpStatus = status;
        this.errorResponse = new ErrorResponse(message);
    }

    protected DomainException(String errorCode, String message, HttpStatus status) {
        this.httpStatus = status;
        this.errorResponse = new ErrorResponse(errorCode, message);
    }
}
