package com.example.data.instructor;

import com.example.data.course.Course;
import com.example.data.course.CourseRepository;
import com.example.data.exception.ResourceNotFoundException;
import com.example.data.instructor.dto.InstructorRequestDto;
import com.example.data.instructor.dto.InstructorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;

    public List<InstructorResponseDto> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(InstructorResponseDto::fromEntity)
                .toList();
    }

    public InstructorResponseDto getInstructorById(Long id) {
        Instructor instructor = findInstructor(id);
        return InstructorResponseDto.fromEntity(instructor);
    }

    @Transactional
    public InstructorResponseDto createInstructor(InstructorRequestDto dto) {
        Instructor instructor = new Instructor(dto.firstName().trim(), dto.lastName().trim());

        if (dto.courseId() != null) {
            Course course = findCourse(dto.courseId());
            instructor.setCourse(course);
        }

        String email = normalize(dto.email());
        String graduatedCollege = normalize(dto.graduatedCollege());

        if (dto.age() != null || email != null || graduatedCollege != null) {
            InstructorDetails details = new InstructorDetails(dto.age(), email, graduatedCollege);
            // Explicitly sync both sides of the relationship
            details.setInstructor(instructor);
            instructor.setInstructorDetails(details);
        }

        Instructor saved = instructorRepository.saveAndFlush(instructor);
        return InstructorResponseDto.fromEntity(saved);
    }

    @Transactional
    public InstructorResponseDto updateInstructor(Long id, InstructorRequestDto dto) {
        Instructor instructor = findInstructor(id);
        instructor.setFirstName(dto.firstName().trim());
        instructor.setLastName(dto.lastName().trim());

        // Update or detach course
        if (dto.courseId() != null) {
            Course course = findCourse(dto.courseId());
            instructor.setCourse(course);
        } else {
            instructor.setCourse(null);
        }

        String email = normalize(dto.email());
        String graduatedCollege = normalize(dto.graduatedCollege());

        InstructorDetails details = instructor.getInstructorDetails();
        if (details == null && (dto.age() != null || email != null || graduatedCollege != null)) {
            InstructorDetails newDetails = new InstructorDetails(dto.age(), email, graduatedCollege);
            newDetails.setInstructor(instructor);
            instructor.setInstructorDetails(newDetails);
        } else if (details != null) {
            details.setAge(dto.age());
            details.setEmail(email);
            details.setGraduatedCollege(graduatedCollege);
        }

        Instructor updated = instructorRepository.saveAndFlush(instructor);
        return InstructorResponseDto.fromEntity(updated);
    }

    @Transactional
    public void deleteInstructor(Long id) {
        Instructor instructor = findInstructor(id);

        if (instructor.getCourse() != null) {
            instructor.setCourse(null);
        }

        instructorRepository.delete(instructor);
    }

    private Instructor findInstructor(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private String normalize(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }
}