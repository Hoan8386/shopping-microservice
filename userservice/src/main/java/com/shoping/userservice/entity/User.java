package com.shoping.userservice.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

/**
 * JPA Entity đại diện cho User trong database local.
 * 
 * Entity này lưu trữ thông tin cơ bản của user trong database của service.
 * Thông tin authentication (password, roles) được lưu trong Keycloak.
 * 
 * Quan hệ với Keycloak:
 * - userId: UUID của user trong Keycloak, dùng để mapping
 * - Password KHÔNG được lưu trong entity này (lưu trong Keycloak)
 * - Authentication được xử lý bởi Keycloak
 * 
 * @author User Service Team
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /** Primary key tự động tăng trong database local */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * UUID của user trong Keycloak Identity Provider.
     * Dùng để mapping giữa local user và Keycloak user.
     * Format: "f47ac10b-58cc-4372-a567-0e02b2c3d479"
     */
    @Column(name = "user_id")
    private String userId;

    /** Email của user - phải là unique và bắt buộc */
    @Column(unique = true, nullable = false)
    private String email;

    /** Username dùng để đăng nhập */
    @Column(name = "username")
    private String username;

    /** Tên đệm của user */
    @Column(name = "first_name")
    private String firstName;

    /** Họ của user */
    @Column(name = "last_name")
    private String lastName;

    /** Ngày sinh của user */
    @Column(name = "dob")
    private LocalDate dob;

    /** Họ và tên đầy đủ của user - bắt buộc */
    @Column(nullable = false)
    private String name;
}
