import { UserService } from './../services/user.service';
import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginDTO } from '../dtos/user/login.dto';


@Component({
  selector: 'app-login',
  //standalone: true,
  //imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;
  phoneNumber: string = '';
  password: string = '';

  constructor(private router: Router, private userService: UserService) {
    this.phoneNumber = '';
    this.password = '';
  }

  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`)
  }

  login() {

    const message = `phoneNumber: ${this.phoneNumber}`
                  + `password: ${this.password}`
    //alert(message);

    debugger
    const loginDTO:LoginDTO = {
      "phone_number": this.phoneNumber,
      "password": this.password,
    }

    this.userService.login(loginDTO).subscribe({
      next: (response: any) => {
        debugger
        if (response && (response.status === 200 || response.status === 201)) {
          //Registered successfully, move to login page
          //this.router.navigate(['/login']);
        } else {
          // before
        }
      },
      complete: () => {
          debugger
      },
      error: (error: any) => {
        alert(`Cannot login, error: ${error.error}`);
      }
    })
  }
}


