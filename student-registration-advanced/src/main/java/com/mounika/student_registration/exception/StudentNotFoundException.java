package com.mounika.student_registration.exception;

/**
 * Thrown when a requested student cannot be found by ID.
 * Maps to HTTP 404 Not Found in GlobalExceptionHandler.
 */
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String message) {
        super(message);
    }
}
