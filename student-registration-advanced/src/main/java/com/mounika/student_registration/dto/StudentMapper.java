package com.mounika.student_registration.dto;

import com.mounika.student_registration.models.Student;

/**
 * Utility mapper class for converting between Student JPA Entity
 * and Student Request/Response DTOs.
 *
 * Keeps conversion logic centralized and clean without introducing heavy reflection overhead.
 */
public final class StudentMapper {

    private StudentMapper() {
        // Prevent instantiation
    }

    /**
     * Converts a StudentRequestDTO into a new Student Entity (for creation).
     */
    public static Student toEntity(StudentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Student.builder()
                .studentName(dto.studentName())
                .courseName(dto.courseName())
                .email(dto.email())
                .age(dto.age())
                .build();
    }

    /**
     * Updates an existing Student entity with fields from StudentRequestDTO.
     */
    public static void updateEntity(Student student, StudentRequestDTO dto) {
        if (student == null || dto == null) {
            return;
        }
        student.setStudentName(dto.studentName());
        student.setCourseName(dto.courseName());
        student.setEmail(dto.email());
        student.setAge(dto.age());
    }

    /**
     * Converts a Student JPA Entity into a StudentResponseDTO.
     */
    public static StudentResponseDTO toResponseDTO(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentResponseDTO(
                student.getStudentId(),
                student.getStudentName(),
                student.getCourseName(),
                student.getEmail(),
                student.getAge(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}
