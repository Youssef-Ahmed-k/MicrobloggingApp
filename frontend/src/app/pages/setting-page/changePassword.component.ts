import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';
import { ChangePasswordDto } from 'src/app/payloads/request/changePassword';

@Component({
  selector: 'app-change-password',
  templateUrl: './changePassword.component.html',
  styleUrls: ['./changePassword.component.css']
})
export class ChangePasswordComponent implements OnInit {
  changePasswordForm: FormGroup;
  changePasswordPayload: ChangePasswordDto;
  oldPasswordErrorMessage: string = '';
  newPasswordErrorMessage: string = '';

  constructor(private toastr: ToastrService, private authService: AuthService) {
    this.changePasswordForm = new FormGroup({
      oldPassword: new FormControl('', Validators.required),
      newPassword: new FormControl('', Validators.required)
    });

    this.changePasswordPayload = { oldPassword: '', newPassword: '' };
  }

  ngOnInit(): void {}

  changePassword() {
    this.oldPasswordErrorMessage = '';
    this.newPasswordErrorMessage = '';

    this.changePasswordPayload.oldPassword = this.changePasswordForm.get('oldPassword')?.value;
    this.changePasswordPayload.newPassword = this.changePasswordForm.get('newPassword')?.value;
    
    this.authService.changePassword(this.changePasswordPayload).subscribe({
      next: () => {
        this.toastr.success('Password changed successfully');
        this.changePasswordForm.reset();
      },
      error: (error) => {
        if (error.status === 500) {
          if (
            error.error.message === "Invalid old password"
          ) {
            this.oldPasswordErrorMessage = error.error.message;
          } else if (
            error.error.message === "Password must start with an uppercase letter and be between 5 to 15 characters."
          ) {
            this.newPasswordErrorMessage = error.error.message;
          } else {
            this.oldPasswordErrorMessage = '';
            this.newPasswordErrorMessage = '';
          }
        } else {
          this.newPasswordErrorMessage = '';
          this.oldPasswordErrorMessage = '';
        }
      },
    });
  }
}
