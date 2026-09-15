package com.shoping.employeeservice.identity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenExchangeParam {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String scope;
}