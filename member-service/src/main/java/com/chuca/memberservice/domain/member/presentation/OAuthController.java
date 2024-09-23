package com.chuca.memberservice.domain.member.presentation;

import com.chuca.memberservice.domain.member.application.dto.LoginDto;
import com.chuca.memberservice.domain.member.application.usecase.SocialLoginUseCase;
import com.chuca.memberservice.global.dto.SocialLoginDto;
import com.chuca.memberservice.global.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {
    private final SocialLoginUseCase socialLoginUsecase;

    // 소셜 로그인
    @PostMapping("/{socialType}/login")
    public ResponseEntity<BaseResponse<LoginDto.Response>> socialLogin(@PathVariable String socialType, @RequestBody @Valid SocialLoginDto.Request request) {
        /*
         * 카카오 소셜 로그인 시 프론트에서 전달 받는 것 (동의 옵션 제외) *
         *
         * 탈퇴를 위해 target_id (회원번호) 반드시 필요
         * 회원번호, 이메일, 전화번호(사업자 번호가 있어야만 가능), 닉네임, 프로필 이미지
         *
         * 네이버 소셜 로그인 시 프론트에서 전달 받는 것 (동의 옵션 제외) *
         *
         * 이메일, 전화번호, 닉네임, 프로필 이미지
         */
        return ResponseEntity.ok(BaseResponse.create(socialLoginUsecase.socialLogin(socialType, request)));

    }

    // 카카오 로그인 탈퇴

    // 네이버 로그인 탈퇴

    // 구글 로그인 탈퇴
}
