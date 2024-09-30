package com.chuca.memberservice.domain.owner.application.usecase;

import com.chuca.memberservice.domain.member.application.dto.LoginDto.Response;
import com.chuca.memberservice.domain.owner.application.dto.LoginDto;
import com.chuca.memberservice.domain.owner.domain.entity.Owner;
import com.chuca.memberservice.domain.owner.domain.service.OwnerService;
import com.chuca.memberservice.global.annotation.UseCase;
import com.chuca.memberservice.global.exception.BadRequestException;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@UseCase
@Transactional
@RequiredArgsConstructor
public class OwnerLoginUseCase {
    private final OwnerService ownerService;
    private final JwtProvider jwtProvider;

    // 로그인
    public LoginDto.Response login(LoginDto.Request request) {
        Owner owner = ownerService.getOwner(request.getBusinessNum());
        String password = request.getPassword();
        boolean result = ownerService.checkPassword(owner, password);

        if(!result)
            throw new BadRequestException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);

        String accessToken = jwtProvider.encodeJwtToken(owner.getId(), owner.getRole()); // 1. 액세스 토큰 발급
        String refreshToken = jwtProvider.encodeJwtRefreshToken(owner.getId(), owner.getRole()); // 2. 리프레시 토큰 발급
        jwtProvider.storeJwtRefreshToken(owner.getId(), owner.getRole(), refreshToken); // 3. 리프레시 redis에 저장

        return new LoginDto.Response(accessToken, refreshToken);
    }

    // 로그아웃
    public boolean logout(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        return ownerService.logout(token);
    }
}
