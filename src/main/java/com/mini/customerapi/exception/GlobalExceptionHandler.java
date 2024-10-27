package com.mini.customerapi.exception;

import com.mini.customerapi.util.CustomerConstant;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles CustomerNotFoundException.
     * Returns a 404 Not Found status with a detailed error message.
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        LOGGER.error("Customer-app::CustomerNotFoundException: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put(CustomerConstant.STATUS, HttpStatus.NOT_FOUND.value());
        body.put(CustomerConstant.ERROR, "Not Found");
        body.put(CustomerConstant.MESSAGE, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles CustomerBadRequestException.
     * Returns a 400 Bad Request status with a detailed error message.
     */
    @ExceptionHandler(CustomerBadRequestException.class)
    public ResponseEntity<Object> handleCustomerBadRequestException(CustomerBadRequestException ex) {
        LOGGER.error("Customer-app::CustomerBadRequestException: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put(CustomerConstant.STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(CustomerConstant.ERROR, "Bad Request");
        body.put(CustomerConstant.MESSAGE, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DuplicateEmailException.
     * Returns a 409 Conflict status when a duplicate email is detected.
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmailException(DuplicateEmailException ex) {
        LOGGER.error("Customer-app::DuplicateEmailException: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put(CustomerConstant.STATUS, HttpStatus.CONFLICT.value());
        body.put(CustomerConstant.ERROR, "Conflict");
        body.put(CustomerConstant.MESSAGE, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Handles ConstraintViolationException directly or wrapped in TransactionSystemException.
     * Returns a 400 Bad Request for database constraint violations.
     */
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof ConstraintViolationException) {
            ConstraintViolationException constraintEx = (ConstraintViolationException) rootCause;
            return handleConstraintViolation(constraintEx);
        }

        LOGGER.error("Customer-app::TransactionSystemException: {}", ex.getMessage(), ex);
        Map<String, Object> body = new HashMap<>();
        body.put(CustomerConstant.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put(CustomerConstant.ERROR, "Internal Server Error");
        body.put(CustomerConstant.MESSAGE, "A transaction error occurred");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles general ConstraintViolationException.
     * Returns a 400 Bad Request for database constraint violations.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        return handleConstraintViolation(ex);
    }

    private ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        LOGGER.error("Customer-app::ConstraintViolationException: {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put(CustomerConstant.STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(CustomerConstant.ERROR, "Bad Request");

        // Collect specific field names and messages
        String fieldErrors = ex.getConstraintViolations().stream()
                .map(violation -> "Field '" + violation.getPropertyPath() + "': " + violation.getMessage())
                .collect(Collectors.joining(", "));

        body.put(CustomerConstant.MESSAGE, fieldErrors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles any other exceptions.
     * Returns a 500 Internal Server Error with a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        LOGGER.error("Customer-app::Unexpected Exception: {}", ex.getMessage(), ex);
        Map<String, Object> body = new HashMap<>();
        body.put(CustomerConstant.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put(CustomerConstant.ERROR, "Internal Server Error");
        body.put(CustomerConstant.MESSAGE, "An unexpected error occurred");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

