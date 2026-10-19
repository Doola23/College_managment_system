package com.example.data.student;

import com.example.data.student.dto.StudentRequestDto;
import com.example.data.student.dto.StudentResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public StudentResponseDto getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDto createStudent(@Valid @RequestBody StudentRequestDto request) {
        return studentService.createStudent(request);
    }

    @PutMapping("/{id}")
    public StudentResponseDto updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequestDto request) {
        return studentService.updateStudent(id, request);
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    public StudentResponseDto enrollCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return studentService.enrollInCourse(studentId, courseId);
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    public StudentResponseDto unenrollCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return studentService.unenrollFromCourse(studentId, courseId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}