import { Component, Inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  successMessage = '';
  isRegisterMode = false;

  constructor(
    @Inject(AuthService) private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  toggleMode(): void {
    this.isRegisterMode = !this.isRegisterMode;
    this.errorMessage = '';
    this.successMessage = '';
    this.cdr.detectChanges();
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.username.trim() || !this.password.trim()) {
      this.errorMessage = 'Please enter both username and password';
      this.cdr.detectChanges();
      return;
    }

    if (this.isRegisterMode) {
      // --- REGISTER ---
      this.authService.register({ username: this.username, password: this.password }).subscribe({
        next: () => {
          this.successMessage = 'Account created successfully! Please sign in.';
          this.isRegisterMode = false;
          this.password = '';
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          this.errorMessage = typeof err.error === 'string' && err.error 
            ? err.error 
            : 'Registration failed. Try again.';
          this.cdr.detectChanges();
        }
      });
    } else {
      // --- LOGIN ---
      this.authService.login({ username: this.username, password: this.password }).subscribe({
        next: () => {
          this.router.navigate(['/instructors']);
        },
        error: (err: any) => {
          console.error('Login error response:', err);
          if (err.status === 401 || err.status === 403) {
            this.errorMessage = 'Invalid username or password';
          } else {
            this.errorMessage = typeof err.error === 'string' && err.error 
              ? err.error 
              : 'Invalid username or password';
          }
          this.cdr.detectChanges(); // Forces Angular to draw the error box immediately
        }
      });
    }
  }
}