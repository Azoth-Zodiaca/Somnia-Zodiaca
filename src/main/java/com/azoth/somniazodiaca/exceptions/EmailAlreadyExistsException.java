package com.azoth.somniazodiaca.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException (String message) {
        super(message);
    }
}
