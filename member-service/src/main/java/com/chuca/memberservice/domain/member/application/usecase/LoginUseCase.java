package com.chuca.memberservice.domain.member.application.usecase;

import com.chuca.memberservice.domain.member.application.dto.LoginDto;
import com.chuca.memberservice.domain.member.domain.entity.Member;
import com.chuca.memberservice.domain.member.domain.service.MemberService;
import com.chuca.memberservice.global.annotation.UseCase;
import com.chuca.memberservice.global.exception.BadRequestException;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@UseCase
@Transactional
@RequiredArgsConstructor
public class LoginUseCase {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    // 로그인
    public LoginDto.Response login(LoginDto.Request request) {
        Member member = memberService.getMember(request.getGeneralId());
        String password = request.getPassword();
        boolean result = memberService.checkPassword(member, password);

        if(!result)
            throw new BadRequestException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);

        String accessToken = jwtProvider.encodeJwtToken(member.getId()); // 1. 액세스 토큰 발급
        String refreshToken = jwtProvider.encodeJwtRefreshToken(member.getId()); // 2. 리프레시 토큰 발급
        jwtProvider.storeJwtRefreshToken(member.getId(), refreshToken); // 3. 리프레시 redis에 저장

        return new LoginDto.Response(accessToken, refreshToken);
    }

    // 로그아웃
    public boolean logout(String token) {
        return memberService.logout(token);
    }
}
