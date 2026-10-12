package com.example.data.instructor.dto;

import com.example.data.instructor.Instructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String courseName;
    private Integer age;
    private String email;
    private String graduatedCollege;

    public static InstructorResponseDto fromEntity(Instructor instructor) {
        if (instructor == null) return null;

        InstructorResponseDto dto = new InstructorResponseDto();
        dto.setId(instructor.getId());
        dto.setFirstName(instructor.getFirstName());
        dto.setLastName(instructor.getLastName());

        if (instructor.getCourse() != null) {
            dto.setCourseName(instructor.getCourse().getCourseName());
        }

        if (instructor.getInstructorDetails() != null) {
            dto.setAge(instructor.getInstructorDetails().getAge());
            dto.setEmail(instructor.getInstructorDetails().getEmail());
            dto.setGraduatedCollege(instructor.getInstructorDetails().getGraduatedCollege());
        }

        return dto;
    }
}