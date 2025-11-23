package com.employee.security;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getMessage();
        if (message != null && message.contains("employee number has already been used")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("This employee number has already been used");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(message != null ? message : "Invalid request");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String msg = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();
        if (msg != null && msg.toLowerCase().contains("travel_allowance")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Travel allowance value is too large for the allowed range");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid data: " + (msg != null ? msg : "data integrity violation"));
    }
}