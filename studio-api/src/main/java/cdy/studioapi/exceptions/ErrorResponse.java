package cdy.studioapi.exceptions;

import java.io.Serializable;

public record ErrorResponse(String errorCode, String message) implements Serializable {
}
