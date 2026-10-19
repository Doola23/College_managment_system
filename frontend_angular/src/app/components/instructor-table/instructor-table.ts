import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InstructorService } from '../../services/instructor';
import { CourseService } from '../../services/course';
import { Instructor } from '../../models/instructor';
import { Course } from '../../models/course';

type InstructorForm = Instructor & {
  courseId: number | null;
  email?: string;
  age?: number | null;
  graduatedCollege?: string;
};

@Component({
  selector: 'app-instructor-table',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './instructor-table.html',
  styleUrls: ['./instructor-table.css']
})
export class InstructorTableComponent implements OnInit {
  private instructorService = inject(InstructorService);
  private courseService = inject(CourseService);
  private cdr = inject(ChangeDetectorRef);

  instructors: Instructor[] = [];
  courses: Course[] = [];

  editingId: number | null = null;

  editForm: InstructorForm = {
    firstName: '',
    lastName: '',
    courseId: null,
    email: '',
    age: null,
    graduatedCollege: ''
  };

  newInstructor: InstructorForm = {
    firstName: '',
    lastName: '',
    courseId: null,
    email: '',
    age: null,
    graduatedCollege: ''
  };

  selectedInstructor: Instructor | null = null;

  ngOnInit(): void {
    this.loadInstructors();
    this.loadCourses();
  }

  loadInstructors(): void {
    this.instructorService.getAll().subscribe({
      next: (data) => {
        this.instructors = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Error fetching instructors:', err)
    });
  }

  loadCourses(): void {
    this.courseService.getAll().subscribe({
      next: (data) => {
        this.courses = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Error fetching courses:', err)
    });
  }

  getCourseName(courseId: number | null | undefined): string {
    if (!courseId) return 'None';
    const match = this.courses.find((c) => c.id === courseId);
    return match ? match.courseName : 'Unknown';
  }

  viewDetails(instructor: Instructor): void {
    this.selectedInstructor = instructor;
    this.cdr.markForCheck();
  }

  closeDetails(): void {
    this.selectedInstructor = null;
    this.cdr.markForCheck();
  }

  startEdit(instructor: Instructor): void {
    if (instructor.id !== undefined) {
      this.editingId = instructor.id;

      const rawCourse = (instructor as any).course;
      const assignedCourseId =
        rawCourse?.id ??
        instructor.courseId ??
        (typeof rawCourse === 'number' ? rawCourse : null);

      this.editForm = {
        id: instructor.id,
        firstName: instructor.firstName,
        lastName: instructor.lastName,
        courseId: assignedCourseId ? Number(assignedCourseId) : null,
        email: instructor.email ?? instructor.instructorDetails?.email ?? '',
        age: instructor.age ?? instructor.instructorDetails?.age ?? null,
        graduatedCollege: instructor.graduatedCollege ?? instructor.instructorDetails?.graduatedCollege ?? ''
      };
    }
  }

  cancelEdit(): void {
    this.editingId = null;
  }

  private preparePayload(form: InstructorForm): any {
    const cleanEmail =
      form.email && form.email.trim().length > 0 ? form.email.trim() : null;
    const cleanCollege =
      form.graduatedCollege && form.graduatedCollege.trim().length > 0
        ? form.graduatedCollege.trim()
        : null;
    const cleanAge =
      form.age !== null && form.age !== undefined && form.age !== ('' as any)
        ? Number(form.age)
        : null;

    let cleanCourseId: number | null = null;
    if (typeof form.courseId === 'number' && !isNaN(form.courseId)) {
      cleanCourseId = form.courseId;
    } else if (typeof form.courseId === 'string' && !isNaN(Number(form.courseId))) {
      cleanCourseId = Number(form.courseId);
    } else if ((form.courseId as any)?.id) {
      cleanCourseId = Number((form.courseId as any).id);
    }

    return {
      firstName: form.firstName.trim(),
      lastName: form.lastName.trim(),
      courseId: cleanCourseId,
      age: cleanAge,
      email: cleanEmail,
      graduatedCollege: cleanCollege
    };
  }

  createInstructor(): void {
    if (!this.newInstructor.firstName.trim() || !this.newInstructor.lastName.trim()) {
      return;
    }

    const payload = this.preparePayload(this.newInstructor);

    this.instructorService.create(payload).subscribe({
      next: () => {
        this.newInstructor = {
          firstName: '',
          lastName: '',
          courseId: null,
          email: '',
          age: null,
          graduatedCollege: ''
        };
        this.loadInstructors();
      },
      error: (err) => {
        console.error('Error creating instructor:', err);
        const msg = err.error?.message || (typeof err.error === 'string' ? err.error : 'Validation failed');
        alert(`Could not save instructor: ${msg}`);
      }
    });
  }

  saveEdit(): void {
    if (this.editingId === null) return;

    const payload = this.preparePayload(this.editForm);

    this.instructorService.update(this.editingId, payload).subscribe({
      next: () => {
        this.editingId = null;
        this.loadInstructors();
      },
      error: (err) => {
        console.error('Error updating instructor:', err);
        const msg = err.error?.message || (typeof err.error === 'string' ? err.error : 'Validation failed');
        alert(`Could not update instructor: ${msg}`);
      }
    });
  }

  deleteInstructor(id: number | undefined): void {
    if (id !== undefined) {
      this.instructorService.delete(id).subscribe({
        next: () => {
          if (this.selectedInstructor?.id === id) {
            this.closeDetails();
          }
          this.loadInstructors();
        },
        error: (err) => console.error('Error deleting instructor:', err)
      });
    }
  }
}