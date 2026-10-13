package com.mounika.student_registration.exception;

/**
 * Thrown when attempting to register or update a student with an email
 * address that is already associated with another student.
 * Maps to HTTP 409 Conflict.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
