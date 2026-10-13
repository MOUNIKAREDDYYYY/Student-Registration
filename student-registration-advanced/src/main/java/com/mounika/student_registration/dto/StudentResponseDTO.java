package com.mounika.student_registration.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) returned to API clients.
 * Encapsulates the public view of a student, including generated IDs
 * and auditing timestamps, without exposing internal database entity mechanics.
 */
public record StudentResponseDTO(
        Long studentId,
        String studentName,
        String courseName,
        String email,
        int age,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
