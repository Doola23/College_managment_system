package com.example.data.instructor;

import com.example.data.instructor.dto.InstructorRequestDto;
import com.example.data.instructor.dto.InstructorResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public InstructorResponseDto createInstructor(@RequestBody InstructorRequestDto request) {
        return instructorService.createInstructor(request);
    }

    @GetMapping
    public List<InstructorResponseDto> getAllInstructors() {
        return instructorService.getAllInstructors();
    }

    @GetMapping("/{id}")
    public InstructorResponseDto getInstructorById(@PathVariable Long id) {
        return instructorService.getInstructorById(id);
    }

    @PutMapping("/{id}")
    public InstructorResponseDto updateInstructor(@PathVariable Long id, @RequestBody InstructorRequestDto request) {
        return instructorService.updateInstructor(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
    }
}