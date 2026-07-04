package com.shoping.userservice.service;


import java.util.List;

import com.shoping.userservice.dto.CreateUserRequestDTO;
import com.shoping.userservice.dto.LoginRequestDto;
import com.shoping.userservice.dto.UserResponseDTO;
import com.shoping.userservice.dto.identity.TokenExchangeResponse;

public interface IUserService {
    UserResponseDTO createUser(CreateUserRequestDTO dto);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, CreateUserRequestDTO dto);

    void deleteUser(Long id);

    TokenExchangeResponse login(LoginRequestDto dto);
}
