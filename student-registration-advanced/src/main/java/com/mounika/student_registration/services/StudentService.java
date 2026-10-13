package com.mounika.student_registration.services;

import com.mounika.student_registration.dto.StudentRequestDTO;
import com.mounika.student_registration.dto.StudentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface defining the business contract for Student management operations.
 *
 * Utilizes DTOs (StudentRequestDTO, StudentResponseDTO) to decouple the external
 * API representation from internal database entities.
 */
public interface StudentService {

    /**
     * Registers a new student after validating email uniqueness.
     *
     * @param requestDTO the student creation payload
     * @return the saved student as a response DTO
     */
    StudentResponseDTO registerStudent(StudentRequestDTO requestDTO);

    /**
     * Retrieves all students with pagination and sorting.
     *
     * @param pageable page and sort criteria
     * @return page of student response DTOs
     */
    Page<StudentResponseDTO> getAllStudents(Pageable pageable);

    /**
     * Retrieves all students as a complete list.
     *
     * @return list of all student response DTOs
     */
    List<StudentResponseDTO> getAllStudents();

    /**
     * Finds a single student by their unique ID.
     *
     * @param studentId the ID of the student
     * @return the student response DTO
     * @throws com.mounika.student_registration.exception.StudentNotFoundException if not found
     */
    StudentResponseDTO getStudentById(Long studentId);

    /**
     * Deletes a student by their unique ID.
     *
     * @param studentId the ID of the student to delete
     * @throws com.mounika.student_registration.exception.StudentNotFoundException if not found
     */
    void deleteStudent(Long studentId);

    /**
     * Updates an existing student record with new information.
     *
     * @param studentId the ID of the student to update
     * @param requestDTO the updated data payload
     * @return the updated student response DTO
     * @throws com.mounika.student_registration.exception.StudentNotFoundException if not found
     * @throws com.mounika.student_registration.exception.EmailAlreadyExistsException if email belongs to another student
     */
    StudentResponseDTO updateStudent(Long studentId, StudentRequestDTO requestDTO);

    /**
     * Dynamic multi-criteria search with pagination and sorting.
     *
     * @param name Optional name keyword filter
     * @param course Optional course name filter
     * @param minAge Optional minimum age filter
     * @param maxAge Optional maximum age filter
     * @param pageable pagination parameters
     * @return page of matching students
     */
    Page<StudentResponseDTO> searchStudents(String name, String course, Integer minAge, Integer maxAge, Pageable pageable);

    // Specialized search queries (return empty list if no matches found)
    List<StudentResponseDTO> searchByAge(int age);
    List<StudentResponseDTO> searchByStudentName(String name);
    List<StudentResponseDTO> searchByCourseName(String courseName);
    List<StudentResponseDTO> searchByCourseAndAge(String courseName, int age);
    List<StudentResponseDTO> searchByCourseOrAge(String courseName, int age);
    List<StudentResponseDTO> searchByAgeGreaterThan(int age);
    List<StudentResponseDTO> searchByAgeBetween(int minAge, int maxAge);
    List<StudentResponseDTO> searchByNameKeyword(String keyword);
    List<StudentResponseDTO> getStudentsSortedByName();

    /**
     * Checks if a given email is already registered.
     */
    boolean checkEmailExists(String email);

    /**
     * Counts how many students are enrolled in a given course.
     */
    long countStudentsByCourse(String courseName);
}
