package com.project.smartbuy.controllers;

import com.project.smartbuy.dtos.*;
import com.project.smartbuy.models.User;
import com.project.smartbuy.responses.LoginResponse;
import com.project.smartbuy.responses.RegisterResponse;
import com.project.smartbuy.services.IUserService;
import com.project.smartbuy.components.LocalizationUtils;
import com.project.smartbuy.utils.MessageKeys;
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
  private final LocalizationUtils localizationUtils;

  @PostMapping("/register")
  // can we register an "admin" user?
  public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDTO,
                                                     BindingResult result) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(FieldError::getDefaultMessage)
          .toList();
        return ResponseEntity.badRequest().body(RegisterResponse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED, errorMessages))
                .build());
      }
      if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
        return ResponseEntity.badRequest().body(RegisterResponse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                .build());
      }
      User user = userService.createUser(userDTO);
      return ResponseEntity.ok(RegisterResponse.builder()
              .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
              .user(user)
              .build());
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegisterResponse.builder()
              .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED, e.getMessage()))
              .build());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
    //Check login information and generate token\
    try {
      String token = String.valueOf(userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword()));

      return ResponseEntity.ok(LoginResponse.builder()
              .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
              .token(token)
              .build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(LoginResponse.builder()
              .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
              .build());
    }
  }
}
