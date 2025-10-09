package com.fundly.common.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<Object> handleExpenseNotFound(ExpenseNotFoundException ex) {
        log.warn("Failed to find expense. With message: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse("Failed to find expense.", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpenseCategoryNotFoundException.class)
    public ResponseEntity<Object> handleExpenseCategoryNotFound(ExpenseCategoryNotFoundException ex) {
        log.warn("Failed to find expense category. With message: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse("Failed to find expense category.", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SavingsGoalNotFoundException.class)
    public ResponseEntity<Object> handleGoalNotFound(SavingsGoalNotFoundException ex) {
        log.warn("Failed to find savings goal. With message: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse("Failed to find savings goal.", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        log.warn("Failed to find user. With message: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse("Failed to find user.", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        log.warn("A user with this email already exists. With message: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse("A user with this email already exists.", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<Object> handleForbiddenOperation(ForbiddenOperationException ex) {
        log.warn("You do not have permission to perform this action. With message: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse("You do not have permission to perform this action.", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        log.error("A database error occurred. With message: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse("A database error occurred.", ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        log.warn("An unexpected error occurred. With message: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorResponse("An unexpected error occurred.", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
