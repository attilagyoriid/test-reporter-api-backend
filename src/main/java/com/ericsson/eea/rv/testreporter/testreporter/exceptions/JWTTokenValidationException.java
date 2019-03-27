package com.ericsson.eea.rv.testreporter.testreporter.exceptions;

public class JWTTokenValidationException extends RuntimeException {

    public JWTTokenValidationException() {
    }

    public JWTTokenValidationException(String message) {
        super(message);
    }

    public JWTTokenValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}