package dev.canverse.studio.api.exceptions;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.getBody().setProperty("invalid-params", mapErrors(ex));
        return ResponseEntity.status(status).body(ex.getBody());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, ServletWebRequest request) {
        var detail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred!");
        detail.setTitle(ex.getClass().getSimpleName());
        detail.setInstance(URI.create(request.getRequest().getRequestURI()));
        this.logger.error("An unexpected error occurred!", ex);
        return ResponseEntity.status(detail.getStatus()).body(detail);
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class, IllegalStateException.class, IndexOutOfBoundsException.class})
    public ResponseEntity<Object> handlePreconditions(RuntimeException ex, ServletWebRequest request) {
        var detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        detail.setInstance(URI.create(request.getRequest().getRequestURI()));
        return ResponseEntity.status(detail.getStatus()).body(detail);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleDomainException(ApiException ex, ServletWebRequest request) {
        ex.setDefaultProblemDetails(request);
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getBody());
    }

    private static Map<String, Object> mapErrors(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream().collect(Collectors.toMap(
                FieldError::getField,
                err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : ""
        ));
    }
}
