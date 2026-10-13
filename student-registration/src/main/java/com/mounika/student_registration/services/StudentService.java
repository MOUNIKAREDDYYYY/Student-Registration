package com.mounika.student_registration.services;

// Import the Student entity model
import com.mounika.student_registration.models.Student;

// Import List collection
import java.util.List;
import java.util.Map;

// Interface defining all business service operations for Student Management
public interface StudentService {

    // Register a new student in the system
    Student registerStudent(Student student);

    // Retrieve all registered students
    List<Student> getAllStudents();

    // Find a student by their unique ID
    Student getStudentById(Long studentId);

    // Delete a student from the database by their ID
    void deleteStudent(Long studentId);

    // Update details of an existing student by ID
    Student updateStudent(Long id, Student student);

    // Search students by their exact age
    List<Student> searchByAge(int age);

    // Search students by their exact name
    List<Student> searchByStudentName(String name);

    // Search students enrolled in a specific course
    List<Student> searchByCourseName(String courseName);

    // Search students matching both course name AND age
    List<Student> searchByCourseAndAge(String courseName, int age);

    // Search students matching either course name OR age
    List<Student> searchByCourseOrAge(String courseName, int age);

    // Search students strictly older than a given age
    List<Student> searchByAgeGreaterThan(int age);

    // Search students within an age range (inclusive)
    List<Student> searchByAgeBetween(int minAge, int maxAge);

    // Search students by name containing a keyword (case-insensitive)
    List<Student> searchByNameKeyword(String keyword);

    // Get all students sorted alphabetically by student name
    List<Student> getStudentsSortedByName();

    // Check whether an email address is already present in the database
    boolean checkEmailExists(String email);

    // Count how many students are enrolled in a specific course
    long countStudentsByCourse(String courseName);

}
