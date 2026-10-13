package com.mounika.student_registration.services;

import com.mounika.student_registration.dto.StudentMapper;
import com.mounika.student_registration.dto.StudentRequestDTO;
import com.mounika.student_registration.dto.StudentResponseDTO;
import com.mounika.student_registration.exception.EmailAlreadyExistsException;
import com.mounika.student_registration.exception.StudentNotFoundException;
import com.mounika.student_registration.models.Student;
import com.mounika.student_registration.repository.StudentRepository;
import com.mounika.student_registration.repository.StudentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for student operations.
 *
 * Annotations:
 * - @Service: Registers this class as a Spring business service bean.
 * - @Transactional(readOnly = true): Default transactional boundary for queries.
 *   Optimizes Hibernate performance by disabling dirty checking on read operations.
 * - Mutating operations explicitly override with @Transactional (read-write).
 */
@Service
@Transactional(readOnly = true)
public class StudentServiceImp implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImp(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Registers a new student.
     * Validates that the email is not already taken before attempting insertion,
     * preventing unhandled database constraint exceptions.
     */
    @Override
    @Transactional
    public StudentResponseDTO registerStudent(StudentRequestDTO requestDTO) {
        if (studentRepository.existsByEmail(requestDTO.email())) {
            throw new EmailAlreadyExistsException(
                    "A student with email '" + requestDTO.email() + "' is already registered."
            );
        }

        Student student = StudentMapper.toEntity(requestDTO);
        Student savedStudent = studentRepository.save(student);
        return StudentMapper.toResponseDTO(savedStudent);
    }

    /**
     * Retrieves students with pagination and sorting.
     */
    @Override
    public Page<StudentResponseDTO> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(StudentMapper::toResponseDTO);
    }

    /**
     * Retrieves all students as a list.
     */
    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentMapper::toResponseDTO)
                .toList();
    }

    /**
     * Finds a student by their primary key ID.
     * Throws StudentNotFoundException (HTTP 404) if no record exists.
     */
    @Override
    public StudentResponseDTO getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .map(StudentMapper::toResponseDTO)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with ID: " + studentId
                ));
    }

    /**
     * Deletes a student by their ID.
     */
    @Override
    @Transactional
    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    /**
     * Updates an existing student record.
     * Validates existence, checks email conflict against other records, and updates fields.
     */
    @Override
    @Transactional
    public StudentResponseDTO updateStudent(Long studentId, StudentRequestDTO requestDTO) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        // Check if new email is taken by another student
        if (studentRepository.existsByEmailAndStudentIdNot(requestDTO.email(), studentId)) {
            throw new EmailAlreadyExistsException(
                    "Email '" + requestDTO.email() + "' is already in use by another student."
            );
        }

        StudentMapper.updateEntity(existingStudent, requestDTO);
        Student updatedStudent = studentRepository.save(existingStudent);
        return StudentMapper.toResponseDTO(updatedStudent);
    }

    /**
     * Dynamic multi-criteria search with pagination and sorting.
     * Uses JPA Criteria API via StudentSpecification.
     */
    @Override
    public Page<StudentResponseDTO> searchStudents(String name, String course, Integer minAge, Integer maxAge, Pageable pageable) {
        Specification<Student> spec = StudentSpecification.filterBy(name, course, minAge, maxAge);
        return studentRepository.findAll(spec, pageable)
                .map(StudentMapper::toResponseDTO);
    }

    // Specialized searches - return empty list instead of throwing 404 when no results match
    @Override
    public List<StudentResponseDTO> searchByAge(int age) {
        return studentRepository.findByAge(age).stream()
                .map(StudentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<StudentResponseDTO> searchByStudentName(String name) {
        return studentRepository.findByStudentName(name).stream()
                .map(StudentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<StudentResponseDTO> searchByCourseName(String courseName) {
        return studentRepository.findByCourseName(courseName).stream()
                .map(StudentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<StudentResponseDTO> searchByCourseAndAge(String courseName, int age) {
        return studentRepository.findByCourseNameAndAge(courseName, age).stream()
                .map(StudentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<StudentResponseDTO> searchByCourseOrAge(String courseName, int age) {
        return studentRepository.findByCourseNameOrAge(courseName, age).stream()
                .map(StudentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<StudentResponseDTO> searchByAgeGreaterThan(int age) {
        return studentRepository.findByAgeGreaterThan(age).stream()
                .map(StudentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<StudentResponseDTO> searchByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge).stream()
                .map(StudentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<StudentResponseDTO> searchByNameKeyword(String keyword) {
        return studentRepository.findByStudentNameContainingIgnoreCase(keyword).stream()
                .map(StudentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<StudentResponseDTO> getStudentsSortedByName() {
        return studentRepository.findAllByOrderByStudentNameAsc().stream()
                .map(StudentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public boolean checkEmailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public long countStudentsByCourse(String courseName) {
        return studentRepository.countByCourseName(courseName);
    }
}
