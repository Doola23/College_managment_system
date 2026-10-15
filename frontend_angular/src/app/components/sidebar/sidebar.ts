import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.css']
})
export class SidebarComponent {
  @Input() activeView: string = 'students';
  @Output() viewChange = new EventEmitter<string>();

  selectView(viewName: string): void {
    this.viewChange.emit(viewName);
  }
}