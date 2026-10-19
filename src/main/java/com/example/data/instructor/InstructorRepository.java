package com.example.data.instructor;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    @Override
    @EntityGraph(attributePaths = {"course", "instructorDetails"})
    List<Instructor> findAll();
}