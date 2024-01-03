package com.project.smartbuy.controllers;

import com.project.smartbuy.dtos.*;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.models.User;
import com.project.smartbuy.services.IUserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

  private final IUserService userService;
  @PostMapping("/register")
  // can we register an "admin" user?
  public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO,
    BindingResult result) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(FieldError::getDefaultMessage)
          .toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }
      if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
        return ResponseEntity.badRequest().body("Password does not match!");
      }
      User user = userService.createUser(userDTO);
      return ResponseEntity.ok(user);
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
    //Check login information and generate token\
    try {
      String token = String.valueOf(userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword()));
      return ResponseEntity.ok(token);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
