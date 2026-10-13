package com.mounika.student_registration.controller;

import com.mounika.student_registration.dto.StudentRequestDTO;
import com.mounika.student_registration.dto.StudentResponseDTO;
import com.mounika.student_registration.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller exposing API endpoints for managing student registrations.
 *
 * Base URI: /api/students
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * POST /api/students
     * Registers a new student.
     * Returns 201 Created on success, or 400 Bad Request if validation fails.
     */
    @PostMapping
    public ResponseEntity<StudentResponseDTO> registerStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO savedStudent = studentService.registerStudent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    /**
     * GET /api/students
     * Retrieves students with pagination and sorting.
     * Example: /api/students?page=0&size=10&sort=studentName,asc
     */
    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
            @PageableDefault(page = 0, size = 10, sort = "studentId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<StudentResponseDTO> students = studentService.getAllStudents(pageable);
        return ResponseEntity.ok(students);
    }

    /**
     * GET /api/students/all
     * Retrieves all students as an unpaged list (useful for dropdowns or exports).
     */
    @GetMapping("/all")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudentsList() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    /**
     * GET /api/students/{id}
     * Retrieves a single student by ID.
     * Note: Previously made duplicate query calls to studentService. Fixed to query once.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * PUT /api/students/{id}
     * Updates an existing student record by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO updatedStudent = studentService.updateStudent(id, requestDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * DELETE /api/students/{id}
     * Deletes a student by ID. Returns 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/students/search
     * Consolidated dynamic search endpoint supporting multi-criteria filtering with pagination.
     * Example: /api/students/search?course=Java&minAge=20&maxAge=30&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<Page<StudentResponseDTO>> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String course,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @PageableDefault(page = 0, size = 10, sort = "studentId") Pageable pageable) {
        return ResponseEntity.ok(studentService.searchStudents(name, course, minAge, maxAge, pageable));
    }

    // --- Specialized search endpoints retained for backward compatibility ---

    @GetMapping("/search/age")
    public ResponseEntity<List<StudentResponseDTO>> searchByAge(@RequestParam int age) {
        return ResponseEntity.ok(studentService.searchByAge(age));
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<StudentResponseDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(studentService.searchByStudentName(name));
    }

    @GetMapping("/search/course")
    public ResponseEntity<List<StudentResponseDTO>> searchByCourse(@RequestParam String courseName) {
        return ResponseEntity.ok(studentService.searchByCourseName(courseName));
    }

    @GetMapping("/search/course-age")
    public ResponseEntity<List<StudentResponseDTO>> searchByCourseAndAge(
            @RequestParam String courseName, @RequestParam int age) {
        return ResponseEntity.ok(studentService.searchByCourseAndAge(courseName, age));
    }

    @GetMapping("/search/course-or-age")
    public ResponseEntity<List<StudentResponseDTO>> searchByCourseOrAge(
            @RequestParam String courseName, @RequestParam int age) {
        return ResponseEntity.ok(studentService.searchByCourseOrAge(courseName, age));
    }

    @GetMapping("/search/older")
    public ResponseEntity<List<StudentResponseDTO>> searchOlderStudents(@RequestParam int age) {
        return ResponseEntity.ok(studentService.searchByAgeGreaterThan(age));
    }

    @GetMapping("/search/age-range")
    public ResponseEntity<List<StudentResponseDTO>> searchByAgeRange(
            @RequestParam int minAge, @RequestParam int maxAge) {
        return ResponseEntity.ok(studentService.searchByAgeBetween(minAge, maxAge));
    }

    @GetMapping("/search/keyword")
    public ResponseEntity<List<StudentResponseDTO>> searchByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(studentService.searchByNameKeyword(keyword));
    }

    @GetMapping("/search/sorted")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsSortedByName() {
        return ResponseEntity.ok(studentService.getStudentsSortedByName());
    }

    @GetMapping("/email-exists")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        return ResponseEntity.ok(studentService.checkEmailExists(email));
    }

    @GetMapping("/course-count")
    public ResponseEntity<Long> countStudentsByCourse(@RequestParam String courseName) {
        return ResponseEntity.ok(studentService.countStudentsByCourse(courseName));
    }
}
