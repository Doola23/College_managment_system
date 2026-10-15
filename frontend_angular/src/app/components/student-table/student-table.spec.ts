import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StudentTableComponent} from './student-table';


describe('StudentTableComponent', () => {
  let component: StudentTableComponent;
  let fixture: ComponentFixture<StudentTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentTableComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(StudentTableComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
