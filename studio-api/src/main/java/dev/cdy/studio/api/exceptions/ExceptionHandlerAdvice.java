package dev.cdy.studio.api.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
}
