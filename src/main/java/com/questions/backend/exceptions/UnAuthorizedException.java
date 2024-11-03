package com.questions.backend.exceptions;

public class UnAuthorizedException extends Exception {
    public UnAuthorizedException(String message) {
        super(message);
    }
}
