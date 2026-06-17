package com.shoping.userservice.command.data;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String module;

    private String apiPath;

    private String method;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;

    public Permission(
            String code,
            String name,
            String module,
            String apiPath,
            String method) {
        this.code = code;
        this.name = name;
        this.module = module;
        this.apiPath = apiPath;
        this.method = method;
    }

}