package cdy.studioapi.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    public final HttpStatus httpStatus;
    public final ErrorResponse errorResponse;

    protected ApiException(String errorCode, String message, HttpStatus status) {
        this.httpStatus = status;
        this.errorResponse = new ErrorResponse(errorCode, message);
    }
}
