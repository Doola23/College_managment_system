package com.example.data.course;

import com.example.data.course.dto.CourseRequestDto;
import com.example.data.course.dto.CourseResponseDto;
import com.example.data.exception.ResourceNotFoundException;
import com.example.data.instructor.Instructor;
import com.example.data.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    public List<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseResponseDto::fromEntity)
                .toList();
    }

    public CourseResponseDto getCourseById(Long id) {
        Course course = findCourse(id);
        return CourseResponseDto.fromEntity(course);
    }

    @Transactional
    public CourseResponseDto createCourse(CourseRequestDto dto) {
        Course course = new Course(dto.courseName());
        Course saved = courseRepository.save(course);
        return CourseResponseDto.fromEntity(saved);
    }

    @Transactional
    public CourseResponseDto updateCourse(Long id, CourseRequestDto dto) {
        Course course = findCourse(id);
        course.setCourseName(dto.courseName());
        // No courseRepository.save(course) needed: Hibernate dirty checking persists changes on transaction commit
        return CourseResponseDto.fromEntity(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = findCourse(id);


        if (course.getInstructors() != null) {
            for (Instructor instructor : course.getInstructors()) {
                instructor.setCourse(null);
            }
        }


        if (course.getStudents() != null) {
            for (Student student : course.getStudents()) {
                student.getCourses().remove(course);
            }
        }

        courseRepository.delete(course);
    }

    // Private helper to avoid repeating the lookup and exception handling
    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }
}