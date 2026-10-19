package com.example.data.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record StudentRequestDto(
        @NotBlank(message = "First name is mandatory")
        @Size(max = 50)
        String firstName,

        @NotBlank(message = "Last name is mandatory")
        @Size(max = 50)
        String lastName,

        Set<Long> courseIds
) {}