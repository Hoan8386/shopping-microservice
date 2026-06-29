package com.shoping.userservice.command.data;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    private String id;

    @Column(unique = true)
    private String keycloakId;

    private String fullName;

    private String phone;

    private String address;

    private String avatar;

    private String gender;

    private LocalDate birthday;

    private String status;

    private Instant createdAt;

    private Instant updatedAt;
}
