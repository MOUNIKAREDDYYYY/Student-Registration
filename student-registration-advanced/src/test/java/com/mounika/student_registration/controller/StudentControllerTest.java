package com.mounika.student_registration.controller;

import com.mounika.student_registration.dto.StudentRequestDTO;
import com.mounika.student_registration.dto.StudentResponseDTO;
import com.mounika.student_registration.exception.StudentNotFoundException;
import com.mounika.student_registration.services.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller slice test using @WebMvcTest.
 * Tests REST endpoint request mappings, bean validation, status codes, and exception handler responses.
 */
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    @DisplayName("POST /api/students returns 201 Created when payload is valid")
    void registerStudent_validPayload_returns201() throws Exception {
        String validJson = """
                {
                    "studentName": "Jane Doe",
                    "courseName": "Mathematics",
                    "email": "jane@example.com",
                    "age": 20
                }
                """;

        StudentResponseDTO responseDTO = new StudentResponseDTO(
                1L,
                "Jane Doe",
                "Mathematics",
                "jane@example.com",
                20,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(studentService.registerStudent(any(StudentRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(1))
                .andExpect(jsonPath("$.studentName").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    @DisplayName("POST /api/students returns 400 Bad Request with field errors when payload is invalid")
    void registerStudent_invalidPayload_returns400() throws Exception {
        // Name too short (< 3 chars), invalid email, age under 18
        String invalidJson = """
                {
                    "studentName": "J",
                    "courseName": "",
                    "email": "invalid-email",
                    "age": 15
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.studentName").exists())
                .andExpect(jsonPath("$.validationErrors.courseName").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.age").exists());
    }

    @Test
    @DisplayName("GET /api/students/{id} returns 200 OK when student exists")
    void getStudentById_found_returns200() throws Exception {
        StudentResponseDTO responseDTO = new StudentResponseDTO(
                1L,
                "Jane Doe",
                "Mathematics",
                "jane@example.com",
                20,
                LocalDateTime.now(),
                null
        );

        when(studentService.getStudentById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/students/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1))
                .andExpect(jsonPath("$.studentName").value("Jane Doe"));
    }

    @Test
    @DisplayName("GET /api/students/{id} returns 404 Not Found when student does not exist")
    void getStudentById_notFound_returns404() throws Exception {
        when(studentService.getStudentById(99L))
                .thenThrow(new StudentNotFoundException("Student not found with ID: 99"));

        mockMvc.perform(get("/api/students/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Student not found with ID: 99"))
                .andExpect(jsonPath("$.path").value("/api/students/99"));
    }

    @Test
    @DisplayName("DELETE /api/students/{id} returns 204 No Content")
    void deleteStudent_returns204() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/students/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
