package com.chuca.memberservice.global.security;

import com.chuca.memberservice.global.exception.BadRequestException;
import com.chuca.memberservice.global.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// 권한이 없을 때 에러 처리해주는 핸들러 (default는 403 Forbidden)
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        final Map<String, Object> body = new HashMap<>();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 예외에 맞는 HTTP 상태 코드 설정
        response.setContentType("application/json");

        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Unauthorized");
        body.put("message", "권한이 존재하지 않습니다."); // 예외에 맞는 메시지 설정
        body.put("path", request.getRequestURI());

        mapper.writeValue(response.getOutputStream(), body);
    }
}
