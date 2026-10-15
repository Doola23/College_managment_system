import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StudentService } from '../../services/student';
import { Student } from '../../models/student';

@Component({
  selector: 'app-student-table',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-table.html',
  styleUrls: ['./student-table.css']
})
export class StudentTableComponent implements OnInit {
  private studentService = inject(StudentService);

  students: Student[] = [];
  
  // Tracks the row
  editingId: number | null = null;
  editForm: Student = { firstName: '', lastName: '' };

  
  newStudent: Student = { firstName: '', lastName: '' };

  ngOnInit(): void {
    this.loadStudents();
  }

 private cdr = inject(ChangeDetectorRef);

loadStudents(): void {
  this.studentService.getAll().subscribe((data) => {
    this.students = data;
    this.cdr.markForCheck(); // Instantly updates UI without zone.js
  });
}

  startEdit(student: Student): void {
    if (student.id !== undefined) {
      this.editingId = student.id;
      this.editForm = { ...student }; // copy
    }
  }

  cancelEdit(): void {
    this.editingId = null;
  }

  saveEdit(): void {
    if (this.editingId !== null) {
      this.studentService.update(this.editingId, this.editForm).subscribe(() => {
        this.editingId = null;
        this.loadStudents();
      });
    }
  }

  deleteStudent(id: number | undefined): void {
    if (id !== undefined) {
      this.studentService.delete(id).subscribe(() => {
        this.loadStudents();
      });
    }
  }

  createStudent(): void {
    if (!this.newStudent.firstName.trim() || !this.newStudent.lastName.trim()) {
      return;
    }

    this.studentService.create(this.newStudent).subscribe(() => {
      this.newStudent = { firstName: '', lastName: '' }; // Reset
      this.loadStudents();
    });
  }
}