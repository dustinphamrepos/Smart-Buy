import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  //standalone: true,
  //imports: [],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;
  phone: string;
  fullName: string;
  dateOfBirth: Date;
  address: string;
  password: string;
  retypePassword: string;
  isAccepted: boolean;

  constructor(private http: HttpClient, private router: Router) {
    this.phone = '';
    this.fullName = '';
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);
    this.address = '';
    this.password = '';
    this.retypePassword = '';
    this.isAccepted = false;

    // inject
  }
  onPhoneChange() {
    console.log(`Phone typed: ${this.phone}`)
  }
  register() {
    const message = `Phone: ${this.phone}`
      + `fullName: ${this.fullName}`
      + `dateOfBirth: ${this.dateOfBirth}`
      + `address: ${this.address}`
      + `password: ${this.password}`
      + `retypePassword: ${this.retypePassword}`
      + `isAccepted: ${this.isAccepted}`;
    //alert(message);
    debugger
    const apiUrl = "http://localhost:8088/api/v1/users/register";
    const registerData = {
      "fullname": this.fullName,
      "phone_number": this.phone,
      "address": this.address,
      "password": this.password,
      "retype_password": this.retypePassword,
      "date_of_birth": this.dateOfBirth,
      "facebook_account_id": 0,
      "google_account_id": 0,
      "role_id": 1
    }
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http.post(apiUrl, registerData, { headers }, )
      .subscribe({
        next: (response: any) => {
          debugger
          if (response && (response.status === 200 || response.status === 201)) {
            //Registered successfully, move to login page
            this.router.navigate(['/login']);
          } else {
            // before
          }
        },
        complete: () => {
            debugger
        },
        error: (error: any) => {
          alert(`Cannot register, error: ${error.error}`);
        }
      });
  }

  // check 2 password match
  checkPasswordsMatch() {
    if (this.password !== this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({ 'passwordMismatch': true });
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors({ 'passwordMismatch': false });
    }
  }

  checkAge() {
    if (this.dateOfBirth) {
      const today = new Date();
      const birthDate = new Date(this.dateOfBirth);
      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }
      if (age < 18) {
        this.registerForm.form.controls['dateOfBirth'].setErrors({ 'invalidAge': true });
      } else {
        this.registerForm.form.controls['dateOfBirth'].setErrors({ 'invalidAge': false });
      }
    }
  }
}

