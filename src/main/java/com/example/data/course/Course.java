package com.example.data.course;

import com.example.data.instructor.Instructor;
import com.example.data.student.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String courseName;

    @OneToMany(mappedBy = "course")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Instructor> instructors = new ArrayList<>();

    @ManyToMany(mappedBy = "courses")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<Student> students = new HashSet<>();

    public Course(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id != null && Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}