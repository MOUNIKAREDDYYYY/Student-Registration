package com.mounika.student_registration.models;

// Import JPA annotations for database table and column mappings
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

// Import Jakarta validation annotations for input constraints
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

// Import Lombok annotations to eliminate boilerplate getter/setter code
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Marks this class as a JPA Entity mapped to a database table
@Entity
// Specifies the table name in the MySQL database
@Table(name = "students")
// Generates a no-argument constructor (required by Hibernate)
@NoArgsConstructor
// Generates a constructor with all arguments
@AllArgsConstructor
// Generates getters, setters, toString, equals, and hashCode automatically
@Data
public class Student {

    // Marks this field as the primary key of the table
    @Id
    // Configures auto-increment strategy for MySQL (AUTO_INCREMENT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Maps this field to the column 'student_id'
    @Column(name = "student_id")
    private Long studentId;

    // Validation: Ensures name is not null and not empty after trimming
    @NotBlank(message = "Student name cannot be empty")
    // Validation: Ensures length is between 3 and 50 characters
    @Size(min = 3, max = 50, message = "Student name must be between 3 and 50 characters")
    // Maps to column 'student_name', NOT NULL, max length 50
    @Column(name = "student_name", nullable = false, length = 50)
    private String studentName;

    // Validation: Ensures course name is not empty
    @NotBlank(message = "Course name cannot be empty")
    // Validation: Ensures course name length is between 2 and 100 characters
    @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
    // Maps to column 'course_name', NOT NULL, max length 100
    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    // Validation: Ensures email is not empty
    @NotBlank(message = "Email cannot be empty")
    // Validation: Checks for valid email address pattern (e.g. user@domain.com)
    @Email(message = "Enter a valid email address")
    // Validation: Limits email length to 100 characters
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    // Maps to column 'email', NOT NULL, UNIQUE constraint to prevent duplicate emails
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // Validation: Minimum age allowed is 18
    @Min(value = 18, message = "Age must be at least 18")
    // Validation: Maximum age allowed is 100
    @Max(value = 100, message = "Age cannot exceed 100")
    // Maps to column 'age', NOT NULL
    @Column(nullable = false)
    private int age;
}
