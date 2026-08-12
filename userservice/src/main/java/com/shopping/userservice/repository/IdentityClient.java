package com.shopping.userservice.repository;

import feign.Body;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.shopping.userservice.dto.identity.TokenExchangeParam;
import com.shopping.userservice.dto.identity.TokenExchangeResponse;
import com.shopping.userservice.dto.identity.UserCreationParam;
import com.shopping.userservice.dto.identity.UserTokenExchangeParam;


//  Muốn tạo user thì cần phải exchanged token từ client sau đó mới tạo được user 
@FeignClient(name = "identity-client", url = "${idp.url}")
public interface IdentityClient {

        @PostMapping(value = "/realms/shopping/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        TokenExchangeResponse exchangeClientToken(@QueryMap() TokenExchangeParam param);

        @PostMapping(value = "admin/realms/shopping/users", consumes = MediaType.APPLICATION_JSON_VALUE)
        ResponseEntity<?> createUser(@RequestBody() UserCreationParam body,
                        @RequestHeader("authorization") String token);

        @PostMapping(value = "/realms/shopping/protocol/openid-connect/token" , consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        TokenExchangeResponse exchangeUserToken(@QueryMap UserTokenExchangeParam param);
}
