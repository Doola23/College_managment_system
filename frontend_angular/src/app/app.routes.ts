import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { InstructorTableComponent } from './components/instructor-table/instructor-table';
import { CourseTableComponent } from './components/course-table/course-table';
import { StudentTableComponent } from './components/student-table/student-table';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'instructors', component: InstructorTableComponent, canActivate: [authGuard] },
  { path: 'courses', component: CourseTableComponent, canActivate: [authGuard] },
  { path: 'students', component: StudentTableComponent, canActivate: [authGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];