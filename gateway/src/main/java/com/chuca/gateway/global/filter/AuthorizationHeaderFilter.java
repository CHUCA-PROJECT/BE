package com.chuca.gateway.global.filter;

import com.chuca.gateway.global.security.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

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

    // 사용자의 헤더에 Authorization 값이 없거나 유효한 토큰이 아니라면 사용자에게 권한이 없다는 401 Unauthorized 코드를 반환
    @Override
    public GatewayFilter apply(Config config) {
        // Prefilter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("AuthorizationHeaderFilter Start: request -> {}", exchange.getRequest());

            HttpHeaders headers = request.getHeaders();
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                return OnError(exchange, "로그인이 필요한 서비스입니다.", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = headers.get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authorizationHeader.replace("Bearer ", ""); // 토큰 추출

            boolean isValid = jwtProvider.validateToken(token);
            if(!isValid) {
                return OnError(exchange, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }

            log.info("AuthorizationHeaderFilter End");
            return chain.filter(exchange);
        };
    }

    // Mono, Flux => Spring WebFlux
    private Mono<Void> OnError(ServerWebExchange exchange, String message, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 에러 응답 데이터를 Map에 추가
        Map<String, Object> errorResponse = Map.of(
                "timestamp", LocalDateTime.now(),
                "message", message,
                "path", exchange.getRequest().getURI().getPath(),
                "status", httpStatus.value()
        );

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(errorResponse))
                .map(response.bufferFactory()::wrap)
                .doOnError(e -> log.error("JSON 변환 중 오류 발생: ", e))
                .flatMap(dataBuffer -> response.writeWith(Mono.just(dataBuffer))); // Mono.just로 감싸기
    }
}
