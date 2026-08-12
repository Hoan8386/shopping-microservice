package com.shopping.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.userservice.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(String userId);
}
