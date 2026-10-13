package com.mounika.student_registration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standardized API error payload for all error responses across the application.
 * Follows consistent REST API design conventions:
 * - status: HTTP status code (e.g. 400, 404, 409, 500)
 * - error: HTTP status text (e.g. "Bad Request", "Not Found")
 * - message: Human-readable error description
 * - path: The request URI where the error occurred
 * - timestamp: When the error occurred
 * - validationErrors: Optional map of field name -> validation failure message (omitted if null)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp,
        Map<String, String> validationErrors
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, path, LocalDateTime.now(), null);
    }

    public static ErrorResponse ofValidation(int status, String error, String message, String path, Map<String, String> errors) {
        return new ErrorResponse(status, error, message, path, LocalDateTime.now(), errors);
    }
}
