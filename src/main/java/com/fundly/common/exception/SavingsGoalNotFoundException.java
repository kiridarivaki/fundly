package com.fundly.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SavingsGoalNotFoundException extends RuntimeException {
    public SavingsGoalNotFoundException(String message) {
        super(message);
    }
}
