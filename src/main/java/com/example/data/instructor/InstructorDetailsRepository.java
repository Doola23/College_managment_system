package com.example.data.instructor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorDetailsRepository extends JpaRepository<InstructorDetails, Long> {
}