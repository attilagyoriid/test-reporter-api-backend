package com.ericsson.eea.rv.testreporter.testreporter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExitException extends RuntimeException {

    public AlreadyExitException() {
    }

    public AlreadyExitException(String message) {
        super(message);
    }

    public AlreadyExitException(String message, Throwable cause) {
        super(message, cause);
    }
}
