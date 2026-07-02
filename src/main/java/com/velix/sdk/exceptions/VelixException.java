package com.velix.sdk.exceptions;

public class VelixException extends RuntimeException {
    private final int statusCode;

    public VelixException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() { return statusCode; }
}
