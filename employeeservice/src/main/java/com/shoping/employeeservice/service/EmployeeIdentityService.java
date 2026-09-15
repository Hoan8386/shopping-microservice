package com.shoping.employeeservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shoping.employeeservice.command.model.EmployeeRequestModel;
import com.shoping.employeeservice.identity.Credential;
import com.shoping.employeeservice.identity.IdentityClient;
import com.shoping.employeeservice.identity.RoleRepresentation;
import com.shoping.employeeservice.identity.TokenExchangeParam;
import com.shoping.employeeservice.identity.UserCreationParam;

@Service
public class EmployeeIdentityService {

    @Autowired
    private IdentityClient identityClient;

    @Value("${idp.client-id}")
    private String clientId;

    @Value("${idp.client-secret}")
    private String clientSecret;

    public String createEmployeeAccount(EmployeeRequestModel model) {
        var token = identityClient.exchangeClientToken(TokenExchangeParam.builder()
                .grant_type("client_credentials")
                .client_id(clientId)
                .client_secret(clientSecret)
                .scope("openid")
                .build());
        String authorization = "Bearer " + token.getAccessToken();

        ResponseEntity<?> response = identityClient.createUser(
                UserCreationParam.builder()
                        .username(model.getEmail())
                        .email(model.getEmail())
                        .firstName(model.getFirstName())
                        .lastName(model.getLastName())
                        .enabled(true)
                        .emailVerified(false)
                        .credentials(List.of(Credential.builder()
                                .type("password")
                                .value(model.getPassword())
                                .temporary(false)
                                .build()))
                        .build(),
                authorization);

        String userId = extractUserId(response);
        RoleRepresentation employeeRole = identityClient.getRealmRole("EMPLOYEE", authorization);
        identityClient.assignRealmRole(userId, List.of(employeeRole), authorization);
        return userId;
    }

    private String extractUserId(ResponseEntity<?> response) {
        List<String> locations = response.getHeaders().get("Location");
        if (locations == null || locations.isEmpty()) {
            throw new IllegalStateException("Location header is missing in the response");
        }
        String location = locations.get(0);
        return location.substring(location.lastIndexOf('/') + 1);
    }
}