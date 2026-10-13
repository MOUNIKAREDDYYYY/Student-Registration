package com.example.studentregistration.exception;

/**
 * Thrown when a student with the given ID cannot be found.
 */
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student with id " + id + " not found");
    }
}
