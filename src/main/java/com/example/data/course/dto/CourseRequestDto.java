package com.example.data.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseRequestDto(
        @NotBlank(message = "Course name is required")
        @Size(max = 100, message = "Course name must not exceed 100 characters")
        String courseName
) {}