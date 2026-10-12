package com.example.data.instructor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "instructor_details")
@Getter
@Setter
@NoArgsConstructor
public class InstructorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer age;
    private String email;
    private String graduatedCollege;

    @OneToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    public InstructorDetails(Integer age, String email, String graduatedCollege) {
        this.age = age;
        this.email = email;
        this.graduatedCollege = graduatedCollege;
    }
}