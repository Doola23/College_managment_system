package com.example.data.instructor.dto;

import com.example.data.instructor.Instructor;
import com.example.data.instructor.InstructorDetails;

public record InstructorResponseDto(
        Long id,
        String firstName,
        String lastName,
        Long courseId,
        Integer age,
        String email,
        String graduatedCollege
) {
    public static InstructorResponseDto fromEntity(Instructor instructor) {
        Long assignedCourseId = instructor.getCourse() != null ? instructor.getCourse().getId() : null;

        Integer age = null;
        String email = null;
        String college = null;

        InstructorDetails details = instructor.getInstructorDetails();
        if (details != null) {
            age = details.getAge();
            email = details.getEmail();
            college = details.getGraduatedCollege();
        }

        return new InstructorResponseDto(
                instructor.getId(),
                instructor.getFirstName(),
                instructor.getLastName(),
                assignedCourseId,
                age,
                email,
                college
        );
    }
}