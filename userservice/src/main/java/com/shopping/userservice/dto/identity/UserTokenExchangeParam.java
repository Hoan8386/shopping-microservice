package com.shopping.userservice.dto.identity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTokenExchangeParam  {
    String grant_type;
    String client_id;
    String client_secret;
    String scope;
    String username;
    String password;
    
}
