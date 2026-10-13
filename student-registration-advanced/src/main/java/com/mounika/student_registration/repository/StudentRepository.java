package com.mounika.student_registration.repository;

import com.mounika.student_registration.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for the Student entity.
 *
 * Extends:
 * - JpaRepository: provides standard CRUD, pagination, and sorting capabilities.
 * - JpaSpecificationExecutor: provides execution of dynamic JPA Specifications (Criteria API).
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    // 1. Find by exact student name
    List<Student> findByStudentName(String studentName);

    // 2. Find by course name (with pageable support)
    List<Student> findByCourseName(String courseName);
    Page<Student> findByCourseName(String courseName, Pageable pageable);

    // 3. Find by age
    List<Student> findByAge(int age);

    // 4. Find by course AND age
    List<Student> findByCourseNameAndAge(String courseName, int age);

    // 5. Find by course OR age
    List<Student> findByCourseNameOrAge(String courseName, int age);

    // 6. Find students older than a given age
    List<Student> findByAgeGreaterThan(int age);

    // 7. Find students between two ages
    List<Student> findByAgeBetween(int minAge, int maxAge);

    // 8. Find names containing a keyword, ignoring case
    List<Student> findByStudentNameContainingIgnoreCase(String keyword);

    // 9. Sort students by name ascending
    List<Student> findAllByOrderByStudentNameAsc();

    // 10. Check whether an email exists
    boolean existsByEmail(String email);

    // Check whether an email exists for any student OTHER than the given ID (useful for updates)
    boolean existsByEmailAndStudentIdNot(String email, Long studentId);

    // 11. Count students in a course
    long countByCourseName(String courseName);
}
