package com.shopping.userservice.service;

import java.util.List;

import com.shopping.userservice.dto.CreateUserRequestDTO;
import com.shopping.userservice.dto.LoginRequestDTO;
import com.shopping.userservice.dto.UserResponseDTO;
import com.shopping.userservice.dto.identity.TokenExchangeResponse;

public interface IUserService {
    UserResponseDTO createUser(CreateUserRequestDTO dto);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUser(Long id, CreateUserRequestDTO dto);
    void deleteUser(Long id);
    TokenExchangeResponse login(LoginRequestDTO dto);
}
