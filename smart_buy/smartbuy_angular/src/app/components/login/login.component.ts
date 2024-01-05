import { TokenService } from './../../services/token.service';
import { UserService } from '../../services/user.service';
import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginDTO } from '../../dtos/user/login.dto';
import { LoginResponse } from '../../responses/user/login.response';


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

  constructor(
    private router: Router,
    private userService: UserService,
    private tokenService: TokenService
  ) {
    this.phoneNumber = '';
    this.password = '';
  }

  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`)
  }

  login() {

    const message = `phoneNumber: ${this.phoneNumber}`
                  + `password: ${this.password}`

    debugger
    const loginDTO:LoginDTO = {
      "phone_number": this.phoneNumber,
      "password": this.password,
    }

    this.userService.login(loginDTO).subscribe({
      next: (response:LoginResponse) => {
        debugger
        const { token } = response;
        this.tokenService.setToken(token);
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

