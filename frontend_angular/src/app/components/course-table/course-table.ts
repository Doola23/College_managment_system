import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../../services/course';
import { Course } from '../../models/course';

@Component({
  selector: 'app-course-table',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './course-table.html',
  styleUrls: ['./course-table.css']
})
export class CourseTableComponent implements OnInit {
  private courseService = inject(CourseService);

  courses: Course[] = [];
  editingId: number | null = null;
  editForm: Course = { courseName: '' };
  newCourse: Course = { courseName: '' };

  ngOnInit(): void {
    this.loadCourses();
  }

private cdr = inject(ChangeDetectorRef);

loadCourses(): void {
  this.courseService.getAll().subscribe((data) => {
    this.courses = data;
    this.cdr.markForCheck(); 
  });
}

  startEdit(course: Course): void {
    if (course.id !== undefined) {
      this.editingId = course.id;
      this.editForm = { ...course };
    }
  }

  cancelEdit(): void {
    this.editingId = null;
  }

  saveEdit(): void {
    if (this.editingId !== null) {
      this.courseService.update(this.editingId, this.editForm).subscribe(() => {
        this.editingId = null;
        this.loadCourses();
      });
    }
  }

  deleteCourse(id: number | undefined): void {
    if (id !== undefined) {
      this.courseService.delete(id).subscribe(() => {
        this.loadCourses();
      });
    }
  }

  createCourse(): void {
    if (!this.newCourse.courseName.trim()) {
      return;
    }

    this.courseService.create(this.newCourse).subscribe(() => {
      this.newCourse = { courseName: '' };
      this.loadCourses();
    });
  }
}