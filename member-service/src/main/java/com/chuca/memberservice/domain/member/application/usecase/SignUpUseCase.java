package com.chuca.memberservice.domain.member.application.usecase;

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
        if(!memberService.checkId(request.getGeneralId()).isCheck()) {
            throw new BadRequestException("이미 가입한 아이디입니다.", HttpStatus.BAD_REQUEST);
        }

        if(!memberService.checkNickname(request.getNickname()).isCheck()) {
            throw new BadRequestException("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.signup(request); // 1. 회원 DB에 저장
        String accessToken = jwtProvider.encodeJwtToken(member.getId()); // 2. access token 발급
        String refreshToken = jwtProvider.encodeJwtRefreshToken(member.getId()); // 3. refresh token 발급
        jwtProvider.storeJwtRefreshToken(member.getId(), refreshToken); // 4. redis에 refresh token 저장
        
        return new SignUpDto.Response(accessToken, refreshToken);
    }
}
