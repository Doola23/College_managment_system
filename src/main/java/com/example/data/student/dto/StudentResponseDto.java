package com.example.data.student.dto;

import com.example.data.course.dto.CourseResponseDto;
import com.example.data.student.Student;

import java.util.Set;
import java.util.stream.Collectors;

public record StudentResponseDto(
        Long id,
        String firstName,
        String lastName,
        Set<CourseResponseDto> courses
) {
    public static StudentResponseDto fromEntity(Student student) {
        Set<CourseResponseDto> courseDtos = student.getCourses() != null
                ? student.getCourses().stream().map(CourseResponseDto::fromEntity).collect(Collectors.toSet())
                : Set.of();

        return new StudentResponseDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                courseDtos
        );
    }
}