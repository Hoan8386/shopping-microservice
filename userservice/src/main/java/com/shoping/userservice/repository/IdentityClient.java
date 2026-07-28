package com.shoping.userservice.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.shoping.userservice.dto.identity.TokenExchangeParam;
import com.shoping.userservice.dto.identity.TokenExchangeResponse;
import com.shoping.userservice.dto.identity.UserCreationParam;
import com.shoping.userservice.dto.identity.UserTokenExchangeParam;
import org.springframework.web.bind.annotation.RequestPart;
/**
 * Feign Client để tích hợp với Keycloak Identity Provider (Realm: shopping
 * microservices).
 * * Keycloak URL được cấu hình trong application.yml thông qua thuộc tính:
 * ${idp.url}
 * * @author User Service Team
 * 
 * @version 1.1
 */
@FeignClient(name = "identity-client", url = "${idp.url}")
public interface IdentityClient {

    /**
     * Lấy client access token từ Keycloak sử dụng Client Credentials Grant.
     * Dữ liệu được truyền dưới dạng Map để Feign nén vào Request Body (Form URL
     * Encoded).
     * * @param param Map chứa: grant_type, client_id, client_secret
     * 
     * @return TokenExchangeResponse chứa access_token dùng cho các thao tác quản
     *         trị
     */
    @PostMapping(value = "/realms/shopping/protocol/openid-connect/token", 
                 consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeClientToken(@RequestPart("param") TokenExchangeParam param);

    /**
     * Tạo user mới trong Keycloak sử dụng Admin REST API.
     * * @param body UserCreationParam chứa thông tin tài khoản cần tạo (username,
     * email, password...)
     * 
     * @param token Bearer token của Client có quyền quản trị (Format: "Bearer
     *              {access_token}")
     * @return ResponseEntity với trạng thái phản hồi từ Keycloak
     */
    @PostMapping(value = "/admin/realms/shopping/users", 
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(
            @RequestBody UserCreationParam body,
            @RequestHeader("Authorization") String token);

    /**
     * Đăng nhập user hệ thống và lấy JWT tokens sử dụng OAuth2 Password Grant.
     * * @param param Map chứa thông tin đăng nhập: grant_type, client_id,
     * client_secret, username, password
     * 
     * @return TokenExchangeResponse chứa access_token, refresh_token của user
     */
    @PostMapping(value = "/realms/shopping/protocol/openid-connect/token", 
                 consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeUserToken(@RequestPart("param") UserTokenExchangeParam param);
}