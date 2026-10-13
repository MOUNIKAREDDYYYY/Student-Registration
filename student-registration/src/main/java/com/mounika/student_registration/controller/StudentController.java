package com.mounika.student_registration.controller;

import com.mounika.student_registration.models.Student;
import com.mounika.student_registration.services.StudentService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

// Marks this class as a REST Controller where methods return JSON responses
@RestController
@RequestMapping("/api/students")   // Base URL path for all endpoints in this controller
public class StudentController {

    // Service dependency injected via constructor
    private final StudentService studentService;

    // Constructor injection for loose coupling and easy unit testing
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // 1. POST /api/students - Register a new student
    @PostMapping
    public ResponseEntity<Student> registerStudent(@Valid @RequestBody Student student) {
        // Save student through service layer
        Student savedStudent = studentService.registerStudent(student);
        // Return HTTP 201 Created status with the saved student object
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    // 2. GET /api/students - Retrieve all registered students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        // Retrieve list of all students from service
        List<Student> students = studentService.getAllStudents();
        // Return HTTP 200 OK with the students list
        return ResponseEntity.ok(students);
    }

    // 3. GET /api/students/{id} - Get a specific student by ID
    // (FIXED: previously called studentService.getStudentById twice; now calls once and returns the student)
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        // Fetch student once from the service layer
        Student student = studentService.getStudentById(id);
        // Return HTTP 200 OK with the fetched student object
        return ResponseEntity.ok(student);
    }

    // 4. DELETE /api/students/{id} - Delete a student by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        // Call service method to delete the student
        studentService.deleteStudent(id);
        // Return HTTP 204 No Content indicating successful deletion
        return ResponseEntity.noContent().build();
    }

    // 5. PUT /api/students/{id} - Update an existing student by ID
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        // Update student details via service layer
        Student updatedStudent = studentService.updateStudent(id, student);
        // Return HTTP 200 OK with updated student details
        return ResponseEntity.ok(updatedStudent);
    }

    // 6. GET /api/students/search/age?age={age} - Search students by age
    @GetMapping("/search/age")
    public ResponseEntity<List<Student>> searchByAge(@RequestParam int age) {
        // Call service to find students by age
        List<Student> students = studentService.searchByAge(age);
        // Return HTTP 200 OK with matching students
        return ResponseEntity.ok(students);
    }

    // 7. GET /api/students/search/name?name={name} - Search students by student name
    @GetMapping("/search/name")
    public ResponseEntity<List<Student>> searchByName(@RequestParam String name) {
        // Call service to find students by exact name
        return ResponseEntity.ok(studentService.searchByStudentName(name));
    }

    // 8. GET /api/students/search/course?courseName={courseName} - Search students by course name
    @GetMapping("/search/course")
    public ResponseEntity<List<Student>> searchByCourse(@RequestParam String courseName) {
        // Call service to find students by course name
        return ResponseEntity.ok(studentService.searchByCourseName(courseName));
    }

    // 9. GET /api/students/search/course-age?courseName={courseName}&age={age} - Search by course AND age
    @GetMapping("/search/course-age")
    public ResponseEntity<List<Student>> searchByCourseAndAge(@RequestParam String courseName, @RequestParam int age) {
        // Call service with AND condition
        return ResponseEntity.ok(studentService.searchByCourseAndAge(courseName, age));
    }

    // 10. GET /api/students/search/course-or-age?courseName={courseName}&age={age} - Search by course OR age
    @GetMapping("/search/course-or-age")
    public ResponseEntity<List<Student>> searchByCourseOrAge(@RequestParam String courseName, @RequestParam int age) {
        // Call service with OR condition
        return ResponseEntity.ok(studentService.searchByCourseOrAge(courseName, age));
    }

    // 11. GET /api/students/search/older?age={age} - Find students older than given age
    @GetMapping("/search/older")
    public ResponseEntity<List<Student>> searchOlderStudents(@RequestParam int age) {
        // Call service for age > given age
        return ResponseEntity.ok(studentService.searchByAgeGreaterThan(age));
    }

    // 12. GET /api/students/search/age-range?minAge={min}&maxAge={max} - Find students in age range
    @GetMapping("/search/age-range")
    public ResponseEntity<List<Student>> searchByAgeRange(@RequestParam int minAge, @RequestParam int maxAge) {
        // Call service for age range search
        return ResponseEntity.ok(studentService.searchByAgeBetween(minAge, maxAge));
    }

    // 13. GET /api/students/search/keyword?keyword={keyword} - Search by partial name keyword
    @GetMapping("/search/keyword")
    public ResponseEntity<List<Student>> searchByNameKeyword(@RequestParam String keyword) {
        // Call service for case-insensitive partial name match
        return ResponseEntity.ok(studentService.searchByNameKeyword(keyword));
    }

    // 14. GET /api/students/search/sorted - Get all students sorted by name
    @GetMapping("/search/sorted")
    public ResponseEntity<List<Student>> getStudentsSortedByName() {
        // Call service for sorted list
        return ResponseEntity.ok(studentService.getStudentsSortedByName());
    }

    // 15. GET /api/students/email-exists?email={email} - Check if email exists
    @GetMapping("/email-exists")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        // Call service to check email existence
        return ResponseEntity.ok(studentService.checkEmailExists(email));
    }

    // 16. GET /api/students/course-count?courseName={courseName} - Count students in course
    @GetMapping("/course-count")
    public ResponseEntity<Long> countStudentsByCourse(@RequestParam String courseName) {
        // Call service to count students by course
        return ResponseEntity.ok(studentService.countStudentsByCourse(courseName));
    }


}
