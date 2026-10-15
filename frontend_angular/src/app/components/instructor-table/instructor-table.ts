import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InstructorService } from '../../services/instructor';
import { Instructor } from '../../models/instructor';

@Component({
  selector: 'app-instructor-table',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './instructor-table.html',
  styleUrls: ['./instructor-table.css']
})
export class InstructorTableComponent implements OnInit {
  private instructorService = inject(InstructorService);
  private cdr = inject(ChangeDetectorRef);

  instructors: Instructor[] = [];
  editingId: number | null = null;
  editForm: Instructor = { firstName: '', lastName: '' };
  newInstructor: Instructor = { firstName: '', lastName: '' };

  // Read-only details view state
  selectedInstructor: Instructor | null = null;

  ngOnInit(): void {
    this.loadInstructors();
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
      this.editForm = { ...instructor };
    }
  }

  cancelEdit(): void {
    this.editingId = null;
  }

  saveEdit(): void {
    if (this.editingId !== null) {
      this.instructorService.update(this.editingId, this.editForm).subscribe(() => {
        this.editingId = null;
        this.loadInstructors();
      });
    }
  }

  deleteInstructor(id: number | undefined): void {
    if (id !== undefined) {
      this.instructorService.delete(id).subscribe(() => {
        if (this.selectedInstructor?.id === id) {
          this.closeDetails();
        }
        this.loadInstructors();
      });
    }
  }

  createInstructor(): void {
    if (!this.newInstructor.firstName.trim() || !this.newInstructor.lastName.trim()) {
      return;
    }

    this.instructorService.create(this.newInstructor).subscribe(() => {
      this.newInstructor = { firstName: '', lastName: '' };
      this.loadInstructors();
    });
  }
}