package com.shoping.userservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shoping.userservice.dto.CreateUserRequestDTO;
import com.shoping.userservice.dto.UserResponseDTO;
import com.shoping.userservice.service.IUserService;

import java.util.List;

/**
 * REST Controller quản lý các endpoint liên quan đến User.
 * 
 * Tất cả các endpoint trong controller này đều YÊU CẦU AUTHENTICATION.
 * Client phải gửi kèm JWT access token trong header Authorization.
 * 
 * Base URL: /api/v1/users
 * 
 * Security: Các endpoint này được bảo vệ bởi Spring Security + Keycloak.
 * 
 * @author User Service Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * Tạo người dùng mới trong hệ thống.
     * 
     * Endpoint này thực hiện:
     * 1. Tạo user trong Keycloak Identity Provider
     * 2. Lưu thông tin user vào database local
     * 
     * @param dto CreateUserRequestDTO chứa thông tin user (email, username,
     *            password, ...)
     * @return ResponseEntity<UserResponseDTO> thông tin user đã tạo (HTTP 200)
     * 
     * @apiNote POST /api/v1/users
     * @apiNote Headers: Authorization: Bearer {access_token}
     * @apiNote Content-Type: application/json
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    /**
     * Lấy danh sách tất cả người dùng trong hệ thống.
     * 
     * @return ResponseEntity<List<UserResponseDTO>> danh sách users (HTTP 200)
     * 
     * @apiNote GET /api/v1/users
     * @apiNote Headers: Authorization: Bearer {access_token}
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Lấy thông tin chi tiết của một người dùng theo ID.
     * 
     * @param id ID của user trong database local
     * @return ResponseEntity<UserResponseDTO> thông tin user (HTTP 200)
     * @throws RuntimeException nếu không tìm thấy user (HTTP 500)
     * 
     * @apiNote GET /api/v1/users/{id}
     * @apiNote Headers: Authorization: Bearer {access_token}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Cập nhật thông tin người dùng.
     * 
     * Lưu ý: Chỉ cập nhật thông tin trong database local.
     * Không cập nhật thông tin trong Keycloak.
     * 
     * @param id  ID của user cần cập nhật
     * @param dto CreateUserRequestDTO chứa thông tin mới
     * @return ResponseEntity<UserResponseDTO> thông tin user sau cập nhật (HTTP
     *         200)
     * @throws RuntimeException nếu không tìm thấy user (HTTP 500)
     * 
     * @apiNote PUT /api/v1/users/{id}
     * @apiNote Headers: Authorization: Bearer {access_token}
     * @apiNote Content-Type: application/json
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody CreateUserRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    /**
     * Xóa người dùng khỏi hệ thống.
     * 
     * Lưu ý: Chỉ xóa khỏi database local.
     * User vẫn tồn tại trong Keycloak và có thể đăng nhập.
     * 
     * @param id ID của user cần xóa
     * @return ResponseEntity<Void> no content (HTTP 204)
     * 
     * @apiNote DELETE /api/v1/users/{id}
     * @apiNote Headers: Authorization: Bearer {access_token}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}