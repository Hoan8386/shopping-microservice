package com.shoping.userservice.dto;


import lombok.Data;

import java.time.LocalDate;

/**
 * DTO cho request tạo hoặc cập nhật user.
 * 
 * DTO này được sử dụng trong:
 * - POST /api/v1/users - Tạo user mới
 * - PUT /api/v1/users/{id} - Cập nhật user
 * 
 * Lưu ý:
 * - Password chỉ được sử dụng khi tạo user mới
 * - Password được hash và lưu trong Keycloak, KHÔNG lưu trong database local
 * - Khi cập nhật user, password field sẽ bị bỏ qua
 * 
 * @author User Service Team
 * @version 1.0
 */
@Data
public class CreateUserRequestDTO {

    /** Email của user - bắt buộc và phải unique */
    private String email;

    /** Username dùng để đăng nhập - bắt buộc */
    private String username;

    /** Tên đệm của user */
    private String firstName;

    /** Họ của user */
    private String lastName;

    /** Ngày sinh của user - format: YYYY-MM-DD */
    private LocalDate dob;

    /** Họ và tên đầy đủ của user - bắt buộc */
    private String name;

    /**
     * Password của user - chỉ dùng khi tạo user mới.
     * Password này sẽ được hash và lưu trong Keycloak.
     * KHÔNG được lưu trong database local.
     */
    private String password;
}