package com.mounika.student_registration.services;

import com.mounika.student_registration.dto.StudentRequestDTO;
import com.mounika.student_registration.dto.StudentResponseDTO;
import com.mounika.student_registration.exception.EmailAlreadyExistsException;
import com.mounika.student_registration.exception.StudentNotFoundException;
import com.mounika.student_registration.models.Student;
import com.mounika.student_registration.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for StudentServiceImp using Mockito.
 * Tests business logic in isolation without touching a real database.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImp studentService;

    private Student student;
    private StudentRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .studentId(1L)
                .studentName("Alice Smith")
                .courseName("Computer Science")
                .email("alice@example.com")
                .age(22)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        requestDTO = new StudentRequestDTO(
                "Alice Smith",
                "Computer Science",
                "alice@example.com",
                22
        );
    }

    @Test
    @DisplayName("registerStudent successfully saves and returns DTO when email is unique")
    void registerStudent_success() {
        when(studentRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponseDTO result = studentService.registerStudent(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.studentId()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("alice@example.com");
        assertThat(result.studentName()).isEqualTo("Alice Smith");

        verify(studentRepository).existsByEmail("alice@example.com");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    @DisplayName("registerStudent throws EmailAlreadyExistsException when email already exists")
    void registerStudent_duplicateEmail_throwsException() {
        when(studentRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> studentService.registerStudent(requestDTO))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("alice@example.com");

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("getStudentById returns StudentResponseDTO when student exists")
    void getStudentById_success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponseDTO result = studentService.getStudentById(1L);

        assertThat(result).isNotNull();
        assertThat(result.studentId()).isEqualTo(1L);
        assertThat(result.studentName()).isEqualTo("Alice Smith");

        verify(studentRepository).findById(1L);
    }

    @Test
    @DisplayName("getStudentById throws StudentNotFoundException when student does not exist")
    void getStudentById_notFound_throwsException() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(999L))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("updateStudent successfully updates record when ID exists and email is not conflicting")
    void updateStudent_success() {
        StudentRequestDTO updateDTO = new StudentRequestDTO(
                "Alice Johnson",
                "Data Science",
                "alice.new@example.com",
                23
        );

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.existsByEmailAndStudentIdNot("alice.new@example.com", 1L)).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponseDTO result = studentService.updateStudent(1L, updateDTO);

        assertThat(result).isNotNull();
        verify(studentRepository).save(student);
    }

    @Test
    @DisplayName("updateStudent throws EmailAlreadyExistsException if another student owns the email")
    void updateStudent_duplicateEmail_throwsException() {
        StudentRequestDTO updateDTO = new StudentRequestDTO(
                "Alice",
                "CS",
                "taken@example.com",
                22
        );

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.existsByEmailAndStudentIdNot("taken@example.com", 1L)).thenReturn(true);

        assertThatThrownBy(() -> studentService.updateStudent(1L, updateDTO))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("taken@example.com");

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("deleteStudent successfully deletes when ID exists")
    void deleteStudent_success() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteStudent throws StudentNotFoundException when ID does not exist")
    void deleteStudent_notFound_throwsException() {
        when(studentRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudent(999L))
                .isInstanceOf(StudentNotFoundException.class);

        verify(studentRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("searchByAge returns empty list (200 OK semantics) without throwing 404")
    void searchByAge_empty_returnsEmptyList() {
        when(studentRepository.findByAge(99)).thenReturn(Collections.emptyList());

        List<StudentResponseDTO> results = studentService.searchByAge(99);

        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("searchStudents returns paginated results matching specification")
    @SuppressWarnings("unchecked")
    void searchStudents_paginated() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> page = new PageImpl<>(List.of(student), pageable, 1);

        when(studentRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<StudentResponseDTO> result = studentService.searchStudents("Alice", "CS", 20, 25, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).studentName()).isEqualTo("Alice Smith");
    }
}
