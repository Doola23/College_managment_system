import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  InstructorTableComponent } from './instructor-table';

describe('InstructorTable', () => {
  let component: InstructorTableComponent;
  let fixture: ComponentFixture<InstructorTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InstructorTableComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(InstructorTableComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
