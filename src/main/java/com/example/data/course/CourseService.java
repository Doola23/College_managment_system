package com.example.data.course;

import com.example.data.course.dto.CourseRequestDto;
import com.example.data.course.dto.CourseResponseDto;
import com.example.data.instructor.Instructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    public List<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseResponseDto::fromEntity)
                .toList();
    }


    public CourseResponseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return CourseResponseDto.fromEntity(course);
    }

    @Transactional
    public CourseResponseDto createCourse(CourseRequestDto dto) {
        Course course = new Course(dto.getCourseName());
        return CourseResponseDto.fromEntity(courseRepository.save(course));
    }

    @Transactional
    public CourseResponseDto updateCourse(Long id, CourseRequestDto dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        course.setCourseName(dto.getCourseName());
        return CourseResponseDto.fromEntity(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        if (course.getInstructors() != null) {
            for (Instructor instructor : course.getInstructors()) {
                instructor.setCourse(null);
            }
        }

        courseRepository.delete(course);
    }
}