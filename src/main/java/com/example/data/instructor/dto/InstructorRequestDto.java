package com.example.data.instructor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InstructorRequestDto(
        @NotBlank(message = "First name is mandatory")
        @Size(max = 50, message = "First name must not exceed 50 characters")
        String firstName,

        @NotBlank(message = "Last name is mandatory")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,

        Long courseId,
        Integer age,
        String email,
        String graduatedCollege
) {}