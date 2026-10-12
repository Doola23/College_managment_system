package com.example.data.student.dto;

import com.example.data.course.dto.CourseResponseDto;
import com.example.data.student.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private List<CourseResponseDto> courses = new ArrayList<>();

    public static StudentResponseDto fromEntity(Student student) {
        if (student == null) return null;

        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());

        if (student.getCourses() != null) {
            dto.setCourses(
                    student.getCourses()
                            .stream()
                            .map(CourseResponseDto::fromEntity)
                            .toList()
            );
        }

        return dto;
    }
}
