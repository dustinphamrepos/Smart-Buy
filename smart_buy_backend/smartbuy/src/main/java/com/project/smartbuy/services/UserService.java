package com.project.smartbuy.services;

import com.project.smartbuy.dtos.UserDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.models.Role;
import com.project.smartbuy.models.User;
import com.project.smartbuy.repositories.RoleRepository;
import com.project.smartbuy.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;
  @Override
  public User createUser(UserDTO userDTO) throws DataNotFoundException {
    String phoneNumber = userDTO.getPhoneNumber();
    if (userRepository.existsByPhoneNumber(phoneNumber)) {
      throw new DataIntegrityViolationException("Phone number already exists.");
    }
    Role existingRole = roleRepository.findById(userDTO.getRoleId())
      .orElseThrow(() -> new DataNotFoundException("Role not found."));
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
    }
    return userRepository.save(newUser);
  }

  @Override
  public String login(String phoneNumber, String password) {
    return null;
  }
}
