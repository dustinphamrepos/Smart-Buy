package com.project.smartbuy.services;

import com.project.smartbuy.dtos.UserDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.models.User;

public interface IUserService {
  User createUser(UserDTO userDTO) throws DataNotFoundException;
  String login(String phoneNumber, String password);
}
