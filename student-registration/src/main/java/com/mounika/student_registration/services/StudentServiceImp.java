package com.mounika.student_registration.services;

import com.mounika.student_registration.exception.StudentNotFoundException;

// Import Student model
import com.mounika.student_registration.models.Student;

// Import repository interface
import com.mounika.student_registration.repository.StudentRepository;

// Import Spring service annotation
import org.springframework.stereotype.Service;

// Import List collection
import java.util.List;
import java.util.Map;

// Marks this class as a Spring Service component managed by the Spring IoC container
@Service
public class StudentServiceImp implements StudentService {

    // Dependency injection of repository via constructor
    private final StudentRepository studentRepository;

    public StudentServiceImp(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // 1. Save and register a new student record into MySQL database
    @Override
    public Student registerStudent(Student student) {
        // Calls JPA repository save method to insert record
        return studentRepository.save(student);
    }

    // 2. Fetch all student records currently stored in the database
    @Override
    public List<Student> getAllStudents() {
        // Calls JPA repository findAll method to return all rows
        return studentRepository.findAll();
    }

    // 3. Find a single student by primary key ID
    @Override
    public Student getStudentById(Long studentId) {
        // Find by ID, or throw StudentNotFoundException if record does not exist
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + studentId
                ));
    }

    // 4. Delete student by ID from the database
    @Override
    public void deleteStudent(Long studentId) {
        // Check if student exists before attempting deletion
        if (!studentRepository.existsById(studentId)) {
            // Throw exception if student does not exist
            throw new StudentNotFoundException(
                    "Student not found with id: " + studentId
            );
        }
        // Delete student from repository
        studentRepository.deleteById(studentId);
    }

    // 5. Update an existing student record by ID
    @Override
    public Student updateStudent(Long studentId, Student student) {
        // Verify that the student exists in the database
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with ID: " + studentId
                ));

        // Update fields with new values provided in the request
        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setCourseName(student.getCourseName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setAge(student.getAge());

        // Save updated entity back to the database
        return studentRepository.save(existingStudent);
    }

    // 6. Search students matching an exact age
    @Override
    public List<Student> searchByAge(int age) {
        // Query repository by age
        List<Student> students = studentRepository.findByAge(age);
        // Throw exception if no students match the specified age
        if (students.isEmpty()) {
            throw new StudentNotFoundException(
                    "No students found with age: " + age
            );
        }
        return students;
    }

    // 7. Search students matching an exact name
    @Override
    public List<Student> searchByStudentName(String name) {
        // Query repository by student name
        List<Student> students = studentRepository.findByStudentName(name);
        // Throw exception if no matching students are found
        if (students.isEmpty()) {
            throw new StudentNotFoundException(
                    "No students found with name: " + name
            );
        }
        return students;
    }

    // 8. Search students enrolled in a specific course
    @Override
    public List<Student> searchByCourseName(String courseName) {
        // Query repository by course name
        List<Student> students = studentRepository.findByCourseName(courseName);
        // Throw exception if no students are enrolled in the course
        if (students.isEmpty()) {
            throw new StudentNotFoundException(
                    "No students found in course: " + courseName
            );
        }
        return students;
    }

    // 9. Search students matching both course name AND age
    @Override
    public List<Student> searchByCourseAndAge(String courseName, int age) {
        // Query repository with AND condition
        List<Student> students = studentRepository.findByCourseNameAndAge(courseName, age);
        // Throw exception if no students match both criteria
        if (students.isEmpty()) {
            throw new StudentNotFoundException(
                    "No students found in course: " + courseName + " with age: " + age
            );
        }
        return students;
    }

    // 10. Search students matching course name OR age
    @Override
    public List<Student> searchByCourseOrAge(String courseName, int age) {
        // Query repository with OR condition
        List<Student> students = studentRepository.findByCourseNameOrAge(courseName, age);
        // Throw exception if no students match either criterion
        if (students.isEmpty()) {
            throw new StudentNotFoundException(
                    "No students found for course: " + courseName + " or age: " + age
            );
        }
        return students;
    }

    // 11. Search students older than a given age
    @Override
    public List<Student> searchByAgeGreaterThan(int age) {
        // Query repository for students with age > given age
        List<Student> students = studentRepository.findByAgeGreaterThan(age);
        // Throw exception if no students match
        if (students.isEmpty()) {
            throw new StudentNotFoundException(
                    "No students found older than age: " + age
            );
        }
        return students;
    }

    // 12. Search students within an age range
    @Override
    public List<Student> searchByAgeBetween(int minAge, int maxAge) {
        // Query repository for age between minAge and maxAge
        List<Student> students = studentRepository.findByAgeBetween(minAge, maxAge);
        // Throw exception if no students fall within the range
        if (students.isEmpty()) {
            throw new StudentNotFoundException(
                    "No students found between ages " + minAge + " and " + maxAge
            );
        }
        return students;
    }

    // 13. Search students by keyword in name (case-insensitive)
    @Override
    public List<Student> searchByNameKeyword(String keyword) {
        // Query repository for partial name match ignoring case
        List<Student> students = studentRepository.findByStudentNameContainingIgnoreCase(keyword);
        // Throw exception if keyword does not match any student
        if (students.isEmpty()) {
            throw new StudentNotFoundException(
                    "No students found matching keyword: " + keyword
            );
        }
        return students;
    }

    // 14. Get all students sorted alphabetically by student name
    @Override
    public List<Student> getStudentsSortedByName() {
        // Query repository sorted by name ascending
        List<Student> students = studentRepository.findAllByOrderByStudentNameAsc();
        // Throw exception if database is empty
        if (students.isEmpty()) {
            throw new StudentNotFoundException(
                    "No students are available to display"
            );
        }
        return students;
    }

    // 15. Check whether an email is already registered in the system
    @Override
    public boolean checkEmailExists(String email) {
        // Query repository to check email existence
        return studentRepository.existsByEmail(email);
    }

    // 16. Count total students enrolled in a particular course
    @Override
    public long countStudentsByCourse(String courseName) {
        // Query repository count by course name
        return studentRepository.countByCourseName(courseName);
    }



}
