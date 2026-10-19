import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CourseRef {
  id: number;
  courseName: string;
}

export interface Student {
  id?: number;
  firstName: string;
  lastName: string;
  courses?: CourseRef[];
}

export interface StudentRequest {
  firstName: string;
  lastName: string;
  courseIds?: number[];
}

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/students';

  getAll(): Observable<Student[]> {
    return this.http.get<Student[]>(this.apiUrl);
  }

  getById(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.apiUrl}/${id}`);
  }

  create(student: StudentRequest): Observable<Student> {
    return this.http.post<Student>(this.apiUrl, student);
  }

  update(id: number, student: StudentRequest): Observable<Student> {
    return this.http.put<Student>(`${this.apiUrl}/${id}`, student);
  }

  enrollCourse(studentId: number, courseId: number): Observable<Student> {
    return this.http.post<Student>(`${this.apiUrl}/${studentId}/courses/${courseId}`, {});
  }

  unenrollCourse(studentId: number, courseId: number): Observable<Student> {
    return this.http.delete<Student>(`${this.apiUrl}/${studentId}/courses/${courseId}`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}