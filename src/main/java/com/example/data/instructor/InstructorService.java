package com.example.data.instructor;

import com.example.data.course.Course;
import com.example.data.course.CourseRepository;
import com.example.data.instructor.dto.InstructorRequestDto;
import com.example.data.instructor.dto.InstructorResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final InstructorDetailsRepository instructorDetailsRepository;

    public InstructorService(InstructorRepository instructorRepository,
                             CourseRepository courseRepository,
                             InstructorDetailsRepository instructorDetailsRepository) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.instructorDetailsRepository = instructorDetailsRepository;
    }


    public List<InstructorResponseDto> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(InstructorResponseDto::fromEntity)
                .toList();
    }


    public InstructorResponseDto getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
        return InstructorResponseDto.fromEntity(instructor);
    }

    @Transactional
    public InstructorResponseDto createInstructor(InstructorRequestDto dto) {
        Instructor instructor = new Instructor(dto.getFirstName(), dto.getLastName());

        if (dto.getCourseId() != null) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + dto.getCourseId()));
            instructor.setCourse(course);
        }

        if (dto.getAge() != null || dto.getEmail() != null || dto.getGraduatedCollege() != null) {
            InstructorDetails details = new InstructorDetails(dto.getAge(), dto.getEmail(), dto.getGraduatedCollege());
            details.setInstructor(instructor);
            instructor.setInstructorDetails(instructorDetailsRepository.save(details));
        }

        return InstructorResponseDto.fromEntity(instructorRepository.save(instructor));
    }

    @Transactional
    public InstructorResponseDto updateInstructor(Long id, InstructorRequestDto dto) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));

        instructor.setFirstName(dto.getFirstName());
        instructor.setLastName(dto.getLastName());

        if (dto.getCourseId() != null) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + dto.getCourseId()));
            instructor.setCourse(course);
        } else {
            instructor.setCourse(null);
        }

        InstructorDetails details = instructor.getInstructorDetails();
        if (details == null && (dto.getAge() != null || dto.getEmail() != null || dto.getGraduatedCollege() != null)) {
            details = new InstructorDetails(dto.getAge(), dto.getEmail(), dto.getGraduatedCollege());
            details.setInstructor(instructor);
            instructor.setInstructorDetails(instructorDetailsRepository.save(details));
        } else if (details != null) {
            details.setAge(dto.getAge());
            details.setEmail(dto.getEmail());
            details.setGraduatedCollege(dto.getGraduatedCollege());
        }

        return InstructorResponseDto.fromEntity(instructorRepository.save(instructor));
    }

    @Transactional
    public void deleteInstructor(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
        instructorRepository.delete(instructor);
    }
}
