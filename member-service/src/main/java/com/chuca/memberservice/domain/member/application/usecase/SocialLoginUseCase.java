package com.chuca.memberservice.domain.member.application.usecase;

import com.chuca.memberservice.domain.member.application.dto.LoginDto;
import com.chuca.memberservice.domain.member.application.dto.SignUpDto;
import com.chuca.memberservice.domain.member.domain.constant.MemberProvider;
import com.chuca.memberservice.domain.member.domain.entity.Member;
import com.chuca.memberservice.domain.member.domain.service.MemberService;
import com.chuca.memberservice.global.annotation.UseCase;
import com.chuca.memberservice.global.dto.SocialLoginDto;
import com.chuca.memberservice.global.exception.BadRequestException;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SocialLoginUseCase {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private static final Map<String, MemberProvider> SOCIAL_TYPE_MAP = Map.of(
            "naver", MemberProvider.NAVER,
            "google", MemberProvider.GOOGLE,
            "kakao", MemberProvider.KAKAO
    );

    // 소셜 로그인
    public LoginDto.Response socialLogin(String socialType, SocialLoginDto.Request request) {
        Member member = memberService.getMemberByEmail(request.getEmail());
        if(member == null) { // 1. 회원가입 필요
            MemberProvider memberProvider = getMemberProvider(socialType);
            member = memberService.socialSignup(memberProvider, request);
        }

        // 토큰 발급
        String accessToken = jwtProvider.encodeJwtToken(member.getId(), member.getRole()); // 2. access token 발급
        String refreshToken = jwtProvider.encodeJwtRefreshToken(member.getId(), member.getRole()); // 3. refresh token 발급
        jwtProvider.storeJwtRefreshToken(member.getId(), member.getRole(), refreshToken); // 4. redis에 refresh token 저장

        return new LoginDto.Response(accessToken, refreshToken);

    }

    public MemberProvider getMemberProvider(String socialType) {
        MemberProvider provider = SOCIAL_TYPE_MAP.get(socialType);
        if (provider == null) {
            throw new BadRequestException(socialType + "은 유효하지 않은 socialType입니다.", HttpStatus.BAD_REQUEST);
        }
        return provider;
    }
}
