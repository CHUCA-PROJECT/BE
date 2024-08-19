package com.chuca.memberservice.domain.member.application.usecase;

import com.chuca.memberservice.domain.member.application.dto.CheckDto;
import com.chuca.memberservice.domain.member.domain.service.MemberService;
import com.chuca.memberservice.global.annotation.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class CheckUseCase {
    private final MemberService memberService;

    // 아이디 중복 확인
    public CheckDto.Response checkId()
    // 휴대폰 인증
}
