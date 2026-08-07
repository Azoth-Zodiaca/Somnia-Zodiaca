package com.azoth.somniazodiaca.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException (String message) {
        super(message);
    }
}
