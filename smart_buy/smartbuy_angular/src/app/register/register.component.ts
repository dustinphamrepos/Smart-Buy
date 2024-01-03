import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';

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

  constructor() {
    this.phone = '';
    this.fullName = '';
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);
    this.address = '';
    this.password = '';
    this.retypePassword = '';
    this.isAccepted = false;
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
    alert(message);
  }

  // check 2 password match
  checkPasswordsMatch() {
    if (this.password !== this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({ 'passwordMismatch': true });
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
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
        this.registerForm.form.controls['dateOfBirth'].setErrors(null);
      }
    }
  }
}

