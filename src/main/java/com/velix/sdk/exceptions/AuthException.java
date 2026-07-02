package com.velix.sdk.exceptions;

public class AuthException extends VelixException {
    public AuthException(String message) { super(message, 401); }
}
