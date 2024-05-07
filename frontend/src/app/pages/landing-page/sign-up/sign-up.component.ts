import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SignUpPayload } from 'src/app/payloads/request/sign-up';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css'],
})
export class SignUpComponent implements OnInit {
  form: FormGroup;
  payload: SignUpPayload;
  usernameErrorMessage: string = '';
  passwordErrorMessage: string = '';
  emailErrorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {
    this.form = new FormGroup({
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      username: new FormControl('', {
        validators: [Validators.required],
        
        updateOn: 'blur',
      }),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required),
    });

    this.payload = {
      firstName: '',
      lastName: '',
      username: '',
      email: '',
      password: '',
    };
  }

  ngOnInit(): void {}

  signUp() {
    this.usernameErrorMessage = '';
    this.passwordErrorMessage = '';
    this.emailErrorMessage = '';

    this.payload.firstName = this.form.get('firstName')?.value;
    this.payload.lastName = this.form.get('lastName')?.value;
    this.payload.username = this.form.get('username')?.value;
    this.payload.email = this.form.get('email')?.value;
    this.payload.password = this.form.get('password')?.value;

    this.authService.signUp(this.payload).subscribe({
      complete: () => {
        this.router.navigate(['/sign-in'], {
          queryParams: { registered: 'true' },
        });
      },
      error: (error) => {
        if (error.status === 500) {
          if (
            error.error.message ===
              'Username must start with at least 2 letters and can have up to 10 characters.' ||
            'Username is already taken.'
          ) {
            this.usernameErrorMessage = error.error.message;
          } else if (
            error.error.message ===
            'Password must start with an uppercase letter and be between 5 to 15 characters.'
          ) {
            this.passwordErrorMessage = error.error.message;
          } else if (
            error.error.message === 'Invalid email format.' ||
            'Email is already registered.'
          ) {
            this.emailErrorMessage = error.error.message;
          } else {
            this.usernameErrorMessage = '';
            this.passwordErrorMessage = '';
            this.emailErrorMessage = '';
          }
        } else {
          this.usernameErrorMessage = '';
          this.passwordErrorMessage = '';
          this.emailErrorMessage = '';
        }
      },
    });
  }

  get username() {
    return this.form.controls['username'];
  }
}
