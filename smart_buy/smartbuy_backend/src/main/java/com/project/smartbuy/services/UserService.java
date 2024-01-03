package com.project.smartbuy.services;

import com.project.smartbuy.components.JwtTokenUtil;
import com.project.smartbuy.dtos.UserDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.exceptions.PermissionDenyException;
import com.project.smartbuy.models.Role;
import com.project.smartbuy.models.User;
import com.project.smartbuy.repositories.RoleRepository;
import com.project.smartbuy.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtTokenUtil jwtTokenUtil;

  private final AuthenticationManager authenticationManager;

  // register user
  @Override
  public User createUser(UserDTO userDTO) throws Exception {
    String phoneNumber = userDTO.getPhoneNumber();
    if (userRepository.existsByPhoneNumber(phoneNumber)) {
      throw new DataIntegrityViolationException("Phone number already exists.");
    }
    Role existingRole = roleRepository.findById(userDTO.getRoleId())
      .orElseThrow(() -> new DataNotFoundException("Role not found."));
    if (existingRole.getName().toUpperCase().equals(Role.ADMIN)) {
      throw new PermissionDenyException("You cannot register a admin account.");
    }
    User newUser = User.builder()
      .fullName(userDTO.getFullName())
      .phoneNumber(userDTO.getPhoneNumber())
      .password(userDTO.getPassword())
      .address(userDTO.getAddress())
      .dateOfBirth(userDTO.getDateOfBirth())
      .facebookAccountId(userDTO.getFacebookAccountId())
      .googleAccountId(userDTO.getGoogleAccountId())
      .role(existingRole)
      .build();
    //Check if you have an account and do not require a password
    if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
      String password = userDTO.getPassword();
      String encoderPassword = passwordEncoder.encode(password);
      newUser.setPassword(encoderPassword);
    }
    return userRepository.save(newUser);
  }


  //login user
  @Override
  public String login(String phoneNumber, String password) throws Exception {
    Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
    if (optionalUser.isEmpty()) {
      throw new DataNotFoundException("Invalid phone number or password.");
    }
    //return optionalUser.get();
    User existingUser = optionalUser.get();
    // check password
    if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
      if (!passwordEncoder.matches(password, existingUser.getPassword())) {
        throw new BadCredentialsException("Wrong phone number or password!");
      }
    }

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
      phoneNumber, password, existingUser.getAuthorities()
    );

    // authenticate with Java Spring Security
    authenticationManager.authenticate(authenticationToken);
    return jwtTokenUtil.generateToken(existingUser);
  }
}
