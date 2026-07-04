package com.shoping.userservice.dto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO cho response trả về thông tin user.
 * 
 * DTO này KHÔNG chứa thông tin nhạy cảm như password.
 * Được sử dụng trong tất cả API responses liên quan đến user.
 * 
 * Sử dụng trong:
 * - GET /api/v1/users - Lấy danh sách users
 * - GET /api/v1/users/{id} - Lấy chi tiết user
 * - POST /api/v1/users - Response sau khi tạo user
 * - PUT /api/v1/users/{id} - Response sau khi cập nhật user
 * 
 * @author User Service Team
 * @version 1.0
 */
@Data
@Builder
public class UserResponseDTO {

    /** ID của user trong database local (auto-increment) */
    private Long id;

    /** UUID của user trong Keycloak (format: UUID string) */
    private String userId;

    /** Email của user */
    private String email;

    /** Username của user */
    private String username;

    /** Tên đệm của user */
    private String firstName;

    /** Họ của user */
    private String lastName;

    /** Ngày sinh của user */
    private LocalDate dob;

    /** Họ và tên đầy đủ của user */
    private String name;
}
