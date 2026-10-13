package com.mounika.student_registration.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing a Student record in the database.
 * Maps to the 'students' table.
 *
 * Annotations used:
 * - @Entity: Marks this class as a JPA managed entity.
 * - @Table(name = "students"): Customizes table name to avoid SQL keyword conflicts.
 * - @EntityListeners(AuditingEntityListener.class): Hooks into Spring Data JPA Auditing
 *   to automatically capture createdAt and updatedAt timestamps without manual setting.
 * - @Data, @NoArgsConstructor, @AllArgsConstructor, @Builder: Lombok boilerplate reducers.
 */
@Entity
@Table(name = "students")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @NotBlank(message = "Student name cannot be empty")
    @Size(min = 3, max = 50, message = "Student name must be between 3 and 50 characters")
    @Column(name = "student_name", nullable = false, length = 50)
    private String studentName;

    @NotBlank(message = "Course name cannot be empty")
    @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Enter a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age cannot exceed 100")
    @Column(nullable = false)
    private int age;

    /**
     * Audit field automatically populated by Spring Data JPA upon record creation.
     * updatable = false ensures the initial creation timestamp is never overwritten on updates.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Audit field automatically updated by Spring Data JPA whenever the entity is modified.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
