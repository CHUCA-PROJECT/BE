package com.chuca.memberservice.domain.member.application.usecase;

import com.chuca.memberservice.domain.member.application.dto.CheckDto;
import com.chuca.memberservice.domain.member.application.dto.SignUpDto;
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
public class SignUpUseCase {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    
    // 회원가입
    public SignUpDto.Response signup(SignUpDto.Request request) {
        if(!checkId(request.getGeneralId()).isCheck()) {
            throw new BadRequestException("이미 가입한 아이디입니다.", HttpStatus.BAD_REQUEST);
        }

        if(!checkNickname(request.getNickname()).isCheck()) {
            throw new BadRequestException("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.signup(request); // 1. 회원 DB에 저장
        String accessToken = jwtProvider.encodeJwtToken(member.getId()); // 2. access token 발급
        String refreshToken = jwtProvider.encodeJwtRefreshToken(member.getId()); // 3. refresh token 발급
        jwtProvider.storeJwtRefreshToken(member.getId(), refreshToken); // 4. redis에 refresh token 저장
        
        return new SignUpDto.Response(accessToken, refreshToken);
    }

    // 아이디 중복 확인
    public CheckDto.Response checkId(String generalId) {
        boolean isDuplicated = memberService.checkId(generalId);
        return new CheckDto.Response(isDuplicated);
    }

    // 닉네임 중복 확인
    public CheckDto.Response checkNickname(String nickname) {
        boolean isDuplicated = memberService.checkNickname(nickname);
        return new CheckDto.Response(isDuplicated);
    }

    // 휴대폰 인증
}
