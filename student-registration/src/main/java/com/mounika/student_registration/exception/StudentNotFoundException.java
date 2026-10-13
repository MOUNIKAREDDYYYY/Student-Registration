package com.mounika.student_registration.exception;

// Custom runtime exception thrown when a student is not found in the database
public class StudentNotFoundException extends RuntimeException {

    // Constructor to pass custom error message to RuntimeException superclass
    public StudentNotFoundException(String message) {
        super(message);
    }
}
