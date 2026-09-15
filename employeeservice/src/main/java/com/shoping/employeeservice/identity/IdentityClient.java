package com.shoping.employeeservice.identity;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import feign.QueryMap;

@FeignClient(name = "identity-client", url = "${idp.url}")
public interface IdentityClient {

    @PostMapping(value = "/realms/shopping/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeClientToken(@QueryMap TokenExchangeParam param);

    @PostMapping(value = "/admin/realms/shopping/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(@RequestBody UserCreationParam body,
            @RequestHeader("Authorization") String authorization);

    @GetMapping("/admin/realms/shopping/roles/{roleName}")
    RoleRepresentation getRealmRole(@PathVariable String roleName,
            @RequestHeader("Authorization") String authorization);

    @PostMapping("/admin/realms/shopping/users/{userId}/role-mappings/realm")
    void assignRealmRole(@PathVariable String userId,
            @RequestBody List<RoleRepresentation> roles,
            @RequestHeader("Authorization") String authorization);
}