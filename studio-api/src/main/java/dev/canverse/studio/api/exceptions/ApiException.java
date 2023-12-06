package dev.canverse.studio.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.ServletWebRequest;

import java.net.URI;

@Getter
public abstract class ApiException extends RuntimeException {

    protected final transient ProblemDetail body;

    protected ApiException(String message) {
        this.body = ProblemDetail.forStatusAndDetail(this.getStatusCode(), message);
    }

    protected HttpStatusCode getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public void setDefaultProblemDetails(ServletWebRequest request) {
        this.body.setInstance(URI.create(request.getRequest().getRequestURI()));
    }
}
