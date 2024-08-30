package com.trace.server.authorizationservice.exception;

import java.util.Date;

public class UserRegistrationException extends RuntimeException{
    public Date getTimestamp() {
        return timestamp;
    }

    private final Date timestamp;
    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
        timestamp = new Date();
    }

    public UserRegistrationException(String message) {
        super(message);
        timestamp = new Date();
    }
}
