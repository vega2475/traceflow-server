package com.trace.server.authorizationservice.payload;


import java.util.Date;

public class UserAlreadyExistsErrorPayload {
    private String message;
    private Date timestamp;

    public UserAlreadyExistsErrorPayload(String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
