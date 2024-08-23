package com.chuca.gateway.global.filter;

import com.chuca.gateway.global.security.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

// Gateway로 요청 들어올 때 JWT 토큰 유효성 검사하는 필터
@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthorizationHeaderFilter(JwtProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    public static class Config {

    }

    // 사용자의 헤더에 Authorization 값이 없거나 유효한 토큰이 아니라면 사용자에게 권한이 없다는 401 Unauthorized 코드를 반환한다.
    @Override
    public GatewayFilter apply(Config config) {
        // Prefilter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("AuthorizationHeaderFilter Start: request -> {}", exchange.getRequest());

            HttpHeaders headers = request.getHeaders();
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                ServerHttpRequest newRequest = request.mutate()
                        .header("memberId", "guest")
                        .build();
                return chain.filter(exchange.mutate().request(newRequest).build());

            }

            String authorizationHeader = headers.get(HttpHeaders.AUTHORIZATION).get(0);

            // JWT 토큰 판별
            String token = authorizationHeader.replace("Bearer", "");

            boolean result = jwtProvider.validateToken(token);

            ServerHttpRequest newRequest = request.mutate()
                    .header("user-id", subject)
                    .build();

            log.info("AuthorizationHeaderFilter End");
            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }
}
