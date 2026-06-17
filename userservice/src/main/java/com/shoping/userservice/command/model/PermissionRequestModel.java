package com.shoping.userservice.command.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequestModel {

    private String id;

    @NotBlank(message = "Permission code must not be blank")
    private String code;

    @NotBlank(message = "Permission name must not be blank")
    @Size(max = 100, message = "Permission name must not exceed 100 characters")
    private String name;

    private String module;

    private String apiPath;

    private String method;
}
