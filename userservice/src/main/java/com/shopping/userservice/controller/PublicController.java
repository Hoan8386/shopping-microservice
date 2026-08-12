package com.shopping.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.userservice.dto.LoginRequestDTO;
import com.shopping.userservice.dto.identity.TokenExchangeResponse;
import com.shopping.userservice.service.IUserService;
import com.shopping.userservice.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/public")
public class PublicController {
    @Autowired IUserService userservice;

    @PostMapping("/login")
    ResponseEntity<TokenExchangeResponse> login(@RequestBody LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(userservice.login(loginRequestDTO));
    }
    
}
