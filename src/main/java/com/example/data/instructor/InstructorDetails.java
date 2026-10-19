package com.example.data.instructor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 100)
    private String graduatedCollege;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    // Explicit constructor omitting 'id' and 'instructor'
    public InstructorDetails(Integer age, String email, String graduatedCollege) {
        this.age = age;
        this.email = email;
        this.graduatedCollege = graduatedCollege;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstructorDetails that = (InstructorDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}