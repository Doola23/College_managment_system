package com.example.data.course.dto;

import com.example.data.course.Course;

public record CourseResponseDto(
        Long id,
        String courseName
) {
    public static CourseResponseDto fromEntity(Course course) {
        return new CourseResponseDto(
                course.getId(),
                course.getCourseName()
        );
    }
}