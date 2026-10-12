package com.example.data.student;

import com.example.data.student.dto.StudentRequestDto;
import com.example.data.student.dto.StudentResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentResponseDto createStudent(@RequestBody StudentRequestDto request) {
        return studentService.createStudent(request);
    }

    @GetMapping
    public List<StudentResponseDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public StudentResponseDto getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public StudentResponseDto updateStudent(@PathVariable Long id, @RequestBody StudentRequestDto request) {
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @PutMapping("/{studentId}/courses/{courseId}")
    public StudentResponseDto enrollCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return studentService.enrollInCourse(studentId, courseId);
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    public StudentResponseDto unenrollCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return studentService.unenrollFromCourse(studentId, courseId);
    }
}