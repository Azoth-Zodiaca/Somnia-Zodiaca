package com.azoth.somniazodiaca.exceptions;

public class GeoNamesUnavailableException extends RuntimeException {

    public GeoNamesUnavailableException(String message) {
        super(message);
    }

    public GeoNamesUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}