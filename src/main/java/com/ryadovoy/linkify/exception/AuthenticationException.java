package com.ryadovoy.linkify.exception;

public class AuthenticationException extends RuntimeException {
    private static final String MESSAGE = "Invalid credentials";

    public AuthenticationException() {
        super(MESSAGE);
    }
}
