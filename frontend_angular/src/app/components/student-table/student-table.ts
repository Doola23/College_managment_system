import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StudentService, Student } from '../../services/student';
import { CourseService } from '../../services/course';
import { Course } from '../../models/course';

@Component({
  selector: 'app-student-table',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-table.html',
  styleUrls: ['./student-table.css']
})
export class StudentTableComponent implements OnInit {
  private studentService = inject(StudentService);
  private courseService = inject(CourseService);
  private cdr = inject(ChangeDetectorRef);

  students: Student[] = [];
  courses: Course[] = [];

  newStudent = {
    firstName: '',
    lastName: ''
  };

  editingId: number | null = null;
  editFirstName = '';
  editLastName = '';

  // Quick enroll selector tracker: { [studentId]: selectedCourseId }
  selectedCourseToEnroll: { [studentId: number]: number | null } = {};

  ngOnInit(): void {
    this.loadStudents();
    this.loadCourses();
  }

  loadStudents(): void {
    this.studentService.getAll().subscribe({
      next: (data) => {
        this.students = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Error fetching students:', err)
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

  createStudent(): void {
    if (!this.newStudent.firstName.trim() || !this.newStudent.lastName.trim()) return;

    this.studentService.create({
      firstName: this.newStudent.firstName.trim(),
      lastName: this.newStudent.lastName.trim(),
      courseIds: []
    }).subscribe({
      next: () => {
        this.newStudent = { firstName: '', lastName: '' };
        this.loadStudents();
      },
      error: (err) => console.error('Error creating student:', err)
    });
  }

  startEdit(student: Student): void {
    if (student.id !== undefined) {
      this.editingId = student.id;
      this.editFirstName = student.firstName;
      this.editLastName = student.lastName;
    }
  }

  cancelEdit(): void {
    this.editingId = null;
  }

  saveEdit(): void {
    if (this.editingId === null) return;

    this.studentService.update(this.editingId, {
      firstName: this.editFirstName.trim(),
      lastName: this.editLastName.trim()
    }).subscribe({
      next: () => {
        this.editingId = null;
        this.loadStudents();
      },
      error: (err) => console.error('Error updating student:', err)
    });
  }

  deleteStudent(id: number | undefined): void {
    if (id !== undefined) {
      this.studentService.delete(id).subscribe({
        next: () => this.loadStudents(),
        error: (err) => console.error('Error deleting student:', err)
      });
    }
  }

  enroll(studentId: number | undefined): void {
    if (!studentId) return;
    const courseId = this.selectedCourseToEnroll[studentId];
    if (!courseId) return;

    this.studentService.enrollCourse(studentId, courseId).subscribe({
      next: () => {
        this.selectedCourseToEnroll[studentId] = null;
        this.loadStudents();
      },
      error: (err) => console.error('Error enrolling course:', err)
    });
  }

  unenroll(studentId: number | undefined, courseId: number): void {
    if (!studentId) return;

    this.studentService.unenrollCourse(studentId, courseId).subscribe({
      next: () => this.loadStudents(),
      error: (err) => console.error('Error unenrolling course:', err)
    });
  }

  getAvailableCourses(student: Student): Course[] {
    const enrolledIds = new Set(student.courses?.map(c => c.id) ?? []);
    return this.courses.filter(c => c.id !== undefined && !enrolledIds.has(c.id));
  }
}