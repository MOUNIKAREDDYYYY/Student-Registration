package com.mounika.student_registration.exception;

// Import Spring HTTP status and response entity
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Import Spring validation exception thrown when @Valid fails
import org.springframework.web.bind.MethodArgumentNotValidException;

// Import Spring exception handler annotations
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Import Map collections for error response formatting
import java.util.LinkedHashMap;
import java.util.Map;

// Global exception interceptor that intercepts exceptions thrown across all REST controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handles StudentNotFoundException - Returns HTTP 404 NOT FOUND with the error message
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleStudentNotFound(StudentNotFoundException exception) {
        // Return 404 status code with the exception's message string
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    // 2. Handles Jakarta Bean Validation errors when @Valid fails on request body - Returns HTTP 400 BAD REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException exception) {
        // Map to store fieldName -> errorMessage pairs
        Map<String, String> errors = new LinkedHashMap<>();

        // Extract each field error and place it in the errors map
        exception.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.putIfAbsent(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        // Return HTTP 400 Bad Request with field errors map
        return ResponseEntity.badRequest().body(errors);
    }

    // 3. Handles general unhandled Java exceptions - Returns HTTP 500 INTERNAL SERVER ERROR
    @ExceptionHandler(java.lang.Exception.class)
    public ResponseEntity<String> handleGeneralException(java.lang.Exception exception) {
        // Return 500 status code with a user-friendly generic message
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong. Please try again.");
    }
}
