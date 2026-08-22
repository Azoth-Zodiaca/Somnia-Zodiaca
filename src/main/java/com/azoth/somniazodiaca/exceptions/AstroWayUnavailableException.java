package com.azoth.somniazodiaca.exceptions;

public class AstroWayUnavailableException extends RuntimeException {

    public AstroWayUnavailableException(String message) {
        super(message);
    }

    public AstroWayUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}