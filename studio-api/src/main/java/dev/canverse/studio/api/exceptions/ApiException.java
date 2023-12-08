package dev.canverse.studio.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

@Getter
public abstract class ApiException extends RuntimeException {
    protected final ProblemDetail body;

    protected ApiException(String message) {
        this.body = ProblemDetail.forStatusAndDetail(this.getHttpStatusCode(), message);
    }

    protected abstract HttpStatusCode getHttpStatusCode();
}
