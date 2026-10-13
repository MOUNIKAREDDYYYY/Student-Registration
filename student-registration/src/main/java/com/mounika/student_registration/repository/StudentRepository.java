package com.mounika.student_registration.repository;

// Import the Student entity model
import com.mounika.student_registration.models.Student;

// Import Spring Data JPA repository base interface
import org.springframework.data.jpa.repository.JpaRepository;

// Import Spring Stereotype annotation
import org.springframework.stereotype.Repository;

// Import Java List utility
import java.util.List;

// Marks this interface as a Spring Data Repository bean
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // 1. Find all students matching an exact student name
    List<Student> findByStudentName(String studentName);

    // 2. Find all students enrolled in a specific course
    List<Student> findByCourseName(String courseName);

    // 3. Find all students matching an exact age
    List<Student> findByAge(int age);

    // 4. Find students matching both course name AND age (AND condition)
    List<Student> findByCourseNameAndAge(String courseName, int age);

    // 5. Find students matching course name OR age (OR condition)
    List<Student> findByCourseNameOrAge(String courseName, int age);

    // 6. Find students older than the given age (age > value)
    List<Student> findByAgeGreaterThan(int age);

    // 7. Find students whose age falls between minAge and maxAge (inclusive range)
    List<Student> findByAgeBetween(int minAge, int maxAge);

    // 8. Search students whose name contains a keyword, ignoring uppercase/lowercase differences
    List<Student> findByStudentNameContainingIgnoreCase(String keyword);

    // 9. Fetch all students sorted in alphabetical order (Ascending) by name
    List<Student> findAllByOrderByStudentNameAsc();

    // 10. Check if a student with the given email address already exists in the database
    boolean existsByEmail(String email);

    // 11. Count total number of students enrolled in a particular course
    long countByCourseName(String courseName);
}
