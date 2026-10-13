package com.mounika.student_registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Student Registration Spring Boot Application.
 *
 * JPA Auditing is configured in {@link com.mounika.student_registration.config.JpaConfig}
 * to prevent slice testing conflicts.
 */
@SpringBootApplication
public class StudentRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentRegistrationApplication.class, args);
    }
}
