package com.shoping.apigateway.Filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

// AbstractGatewayFilterFactory là một class có sẵn của Spring Cloud Gateway, dùng để bạn tự tạo Custom Filter cho API Gateway.
// https://www.baeldung.com/spring-cloud-custom-gateway-filters
@Component
public class JwtHeaderFilter extends AbstractGatewayFilterFactory<JwtHeaderFilter.Config> {

    public JwtHeaderFilter() {
        super(JwtHeaderFilter.Config.class);
    }
    // Lấy thông tin user từ JWT đã được xác thực ở API Gateway → lấy sub và
    // preferred_username → đưa chúng vào HTTP Header → gửi request tiếp tục xuống
    // microservice.
    // Request cũ
    // ↓
    // thêm User ID + Username
    // ↓
    // Request mới
    // ↓
    // đưa Request mới vào Exchange
    // ↓
    // gửi Exchange mới xuống Microservice

    // Mục đích là bên userservice có thể lấy ra user id và user name để thực hiện
    // các thao tác khác
    @Override
    public GatewayFilter apply(JwtHeaderFilter.Config config) {
        return (exchange, chain) -> {
            return exchange.getPrincipal().flatMap(principal -> {
                if (principal instanceof JwtAuthenticationToken jwtAuth) {
                    var claims = jwtAuth.getToken().getClaims();
                    var userId = claims.get("sub").toString();
                    var userName = claims.get("preferred_username").toString();
                    var email = claims.get("email").toString();
                    var firstName = claims.get("given_name").toString();
                    var lastName = claims.get("family_name").toString();
                    System.out.print("userName" + userName);
                    System.out.print("userId" + userId);

                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-User-Id", userId)
                            .header("X-Username", userName)
                            .header("X-User-Email", email)
                            .header("X-First-Name", firstName)
                            .header("X-Last-Name", lastName)
                            .build();
                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                    return chain.filter(mutatedExchange);
                }
                return chain.filter(exchange);
            });
        };
    }

    static class Config {

    }
}