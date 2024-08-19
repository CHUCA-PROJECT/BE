package com.chuca.memberservice.domain.member.application.usecase;

import com.chuca.memberservice.domain.member.application.dto.LoginDto;
import com.chuca.memberservice.global.annotation.UseCase;
import com.chuca.memberservice.global.exception.BadRequestException;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ReissueUseCase {
    private final JwtProvider jwtProvider;

    // 토큰 재발급
    public LoginDto.Response regenerateToken(String token) {
        if (token != null && jwtProvider.validateToken(token)) {
            return jwtProvider.regenerateToken(token);
        }
        throw new BadRequestException("RefreshToken이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
