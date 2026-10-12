package com.example.data.student;

import com.example.data.course.Course;
import com.example.data.course.CourseRepository;
import com.example.data.student.dto.StudentRequestDto;
import com.example.data.student.dto.StudentResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // Inherits readOnly = true
    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponseDto::fromEntity)
                .toList();
    }


    public StudentResponseDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return StudentResponseDto.fromEntity(student);
    }

    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto dto) {
        Student student = new Student(dto.getFirstName(), dto.getLastName());

        if (dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            List<Course> courses = courseRepository.findAllById(dto.getCourseIds());
            student.setCourses(courses);
        }

        return StudentResponseDto.fromEntity(studentRepository.save(student));
    }

    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());

        if (dto.getCourseIds() != null) {
            List<Course> courses = courseRepository.findAllById(dto.getCourseIds());
            student.setCourses(courses);
        }

        return StudentResponseDto.fromEntity(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        studentRepository.delete(student);
    }

    @Transactional
    public StudentResponseDto enrollInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
        }

        return StudentResponseDto.fromEntity(studentRepository.save(student));
    }

    @Transactional
    public StudentResponseDto unenrollFromCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        student.getCourses().remove(course);
        return StudentResponseDto.fromEntity(studentRepository.save(student));
    }
}
