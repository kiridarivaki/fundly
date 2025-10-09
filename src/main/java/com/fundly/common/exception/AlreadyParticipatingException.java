package com.fundly.common.exception;

public class AlreadyParticipatingException extends RuntimeException {
    public AlreadyParticipatingException(String message) {
        super(message);
    }
}
