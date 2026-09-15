package com.shoping.employeeservice.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenExchangeResponse {
    @JsonProperty("access_token")
    private String accessToken;
}