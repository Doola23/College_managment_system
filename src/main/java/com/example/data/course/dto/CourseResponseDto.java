package com.example.data.course.dto;

import com.example.data.course.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {

    private Long id;
    private String courseName;

    public static CourseResponseDto fromEntity(Course course) {
        if (course == null) return null;
        return new CourseResponseDto(course.getId(), course.getCourseName());
    }
}