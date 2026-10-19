package com.example.data.student;

import com.example.data.course.Course;
import com.example.data.course.CourseRepository;
import com.example.data.exception.ResourceNotFoundException;
import com.example.data.student.dto.StudentRequestDto;
import com.example.data.student.dto.StudentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponseDto::fromEntity)
                .toList();
    }

    public StudentResponseDto getStudentById(Long id) {
        Student student = findStudent(id);
        return StudentResponseDto.fromEntity(student);
    }

    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto dto) {
        Student student = new Student(dto.firstName(), dto.lastName());

        if (dto.courseIds() != null && !dto.courseIds().isEmpty()) {
            List<Course> courses = courseRepository.findAllById(dto.courseIds());
            if (courses.size() != dto.courseIds().size()) {
                throw new ResourceNotFoundException("One or more course IDs could not be found");
            }
            courses.forEach(student::addCourse);
        }

        Student saved = studentRepository.save(student);
        return StudentResponseDto.fromEntity(saved);
    }

    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto dto) {
        Student student = findStudent(id);
        student.setFirstName(dto.firstName());
        student.setLastName(dto.lastName());

        if (dto.courseIds() != null) {
            List<Course> courses = courseRepository.findAllById(dto.courseIds());
            if (courses.size() != dto.courseIds().size()) {
                throw new ResourceNotFoundException("One or more course IDs could not be found");
            }

            // Cleanly clear existing and re-add to keep both sides synchronized
            Set<Course> currentCourses = new HashSet<>(student.getCourses());
            currentCourses.forEach(student::removeCourse);
            courses.forEach(student::addCourse);
        }

        // Dirty checking automatically flushes changes on commit
        return StudentResponseDto.fromEntity(student);
    }

    @Transactional
    public StudentResponseDto enrollInCourse(Long studentId, Long courseId) {
        Student student = findStudent(studentId);
        Course course = findCourse(courseId);

        student.addCourse(course);

        Student saved = studentRepository.saveAndFlush(student);
        return StudentResponseDto.fromEntity(saved);
    }

    @Transactional
    public StudentResponseDto unenrollFromCourse(Long studentId, Long courseId) {
        Student student = findStudent(studentId);
        Course course = findCourse(courseId);

        student.removeCourse(course);

        Student saved = studentRepository.saveAndFlush(student);
        return StudentResponseDto.fromEntity(saved);
    }
    @Transactional
    public void deleteStudent(Long id) {
        Student student = findStudent(id);


        Set<Course> courses = new HashSet<>(student.getCourses());
        courses.forEach(student::removeCourse);

        studentRepository.delete(student);
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }
}
