import { Component } from '@angular/core';
import { SidebarComponent } from './components/sidebar/sidebar';
import { StudentTableComponent } from './components/student-table/student-table';
import { CourseTableComponent } from './components/course-table/course-table';
import { InstructorTableComponent } from './components/instructor-table/instructor-table';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    SidebarComponent,
    StudentTableComponent,
    InstructorTableComponent,
    CourseTableComponent
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class AppComponent {
  title = 'frontend_angular';
  currentView: string = 'students';

  onViewChange(view: string): void {
    this.currentView = view;
  }
}
