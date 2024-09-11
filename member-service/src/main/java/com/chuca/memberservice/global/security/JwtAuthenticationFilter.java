package com.chuca.memberservice.global.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

// Jwt 토큰으로 인증하는 필터
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtProvider jwtProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;

    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 토큰 가져오기
        String token = jwtProvider.resolveToken(request);
        String requestURI = request.getRequestURI();

        // 토큰이 존재 여부 + 토큰 검증
        if (StringUtils.isNotEmpty(token) && jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);   // 권한

            // security 세션에 등록
            log.info("security 세션에 등록 ");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else if(StringUtils.isNotEmpty(token) && !jwtProvider.validateToken(token)){
            log.debug("유효한 JWT 토큰이 없습니다, uri: {} ", requestURI);
            throw new ExpiredJwtException(null, null, "유효한 JWT 토큰이 없습니다");
        }

        chain.doFilter(request, response);
    }
}
