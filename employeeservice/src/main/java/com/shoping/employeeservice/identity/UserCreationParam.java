package com.shoping.employeeservice.identity;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreationParam {
    private String username;
    private boolean enabled;
    private String email;
    private boolean emailVerified;
    private String firstName;
    private String lastName;
    private List<Credential> credentials;
}