package com.mounika.student_registration.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for creating or updating a student.
 * Decouples the external REST API payload from internal JPA entity structure.
 * Prevents mass-assignment vulnerabilities (e.g. client cannot pass or alter studentId).
 *
 * Implemented using a modern Java record for immutability and concise syntax.
 */
public record StudentRequestDTO(

        @NotBlank(message = "Student name cannot be empty")
        @Size(min = 3, max = 50, message = "Student name must be between 3 and 50 characters")
        String studentName,

        @NotBlank(message = "Course name cannot be empty")
        @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
        String courseName,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Enter a valid email address")
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        String email,

        @Min(value = 18, message = "Age must be at least 18")
        @Max(value = 100, message = "Age cannot exceed 100")
        int age
) {}
