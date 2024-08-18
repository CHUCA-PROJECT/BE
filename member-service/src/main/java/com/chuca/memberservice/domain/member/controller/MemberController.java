package com.chuca.memberservice.domain.member.controller;

import com.chuca.memberservice.domain.member.dto.CheckDto;
import com.chuca.memberservice.domain.member.dto.LoginDto;
import com.chuca.memberservice.domain.member.dto.OAuthLoginDto;
import com.chuca.memberservice.domain.member.dto.SignUpDto;
import com.chuca.memberservice.domain.member.service.MemberService;
import com.chuca.memberservice.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 휴대전화 인증


    // 아이디 중복 확인
    @GetMapping("/check-id")
    public ResponseEntity<BaseResponse<CheckDto.Response>> checkId(@RequestBody @Validated CheckDto.idRequest request) {
        return ResponseEntity.ok(BaseResponse.create(memberService.checkId(request.getGeneralId())));
    }

    // 일반 회원가입
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<SignUpDto.Response>> signup(@RequestBody @Validated SignUpDto.Request request) { // 추후에 이미지도 처리 해야함
        return ResponseEntity.ok(BaseResponse.create(memberService.signup(request)));
    }

    // 일반 로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginDto.Response>> login(@RequestBody @Validated LoginDto.Request request) {
        return ResponseEntity.ok(BaseResponse.create(memberService.login(request)));
    }
}
