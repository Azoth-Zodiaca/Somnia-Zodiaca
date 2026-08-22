package com.azoth.somniazodiaca.exceptions;

public class GeoNamesRateLimitException extends RuntimeException {

    public GeoNamesRateLimitException(String message) {
        super(message);
    }
}