package com.example.data.instructor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorRequestDto {

    private String firstName;
    private String lastName;
    private Long courseId;
    private Integer age;
    private String email;
    private String graduatedCollege;
}