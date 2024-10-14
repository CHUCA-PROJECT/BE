package com.chuca.memberservice.domain.member.presentation;

import com.chuca.memberservice.domain.member.application.dto.CheckDto;
import com.chuca.memberservice.domain.member.application.dto.LoginDto;
import com.chuca.memberservice.domain.member.application.dto.SignUpDto;
import com.chuca.memberservice.domain.member.application.usecase.LoginUseCase;
import com.chuca.memberservice.domain.member.application.usecase.ReissueUseCase;
import com.chuca.memberservice.domain.member.application.usecase.SignUpUseCase;
import com.chuca.memberservice.domain.member.application.usecase.SocialLoginUseCase;
import com.chuca.memberservice.global.dto.EmailDto;
import com.chuca.memberservice.global.dto.SocialLoginDto;
import com.chuca.memberservice.global.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/member")
public class MemberAuthController {
    private final SocialLoginUseCase socialLoginUsecase;
    private final SignUpUseCase signUpUseCase;
    private final LoginUseCase loginUseCase;
    private final ReissueUseCase reissueUseCase;

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

    // 휴대전화 인증 (일단 메일 인증 사용 -> 리팩토링 단계에서 휴대전화 인증 사용할 예정)

    // 메일 인증
    @PostMapping("/check-email")
    public ResponseEntity<BaseResponse<String>> checkEmail(@RequestBody @Valid EmailDto.Request request) {
        return ResponseEntity.ok(BaseResponse.create(signUpUseCase.checkMail(request.getEmail())));
    }

    // 아이디 중복 확인
    @PostMapping("/check-id")
    public ResponseEntity<BaseResponse<CheckDto.Response>> checkId(@RequestBody @Validated CheckDto.IdRequest request) {
        return ResponseEntity.ok(BaseResponse.create(signUpUseCase.checkId(request.getGeneralId())));
    }

    // 일반 회원가입
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<SignUpDto.Response>> signup(@RequestBody @Validated SignUpDto.Request request) { // 추후에 이미지도 처리 해야함
        return ResponseEntity.ok(BaseResponse.create(signUpUseCase.signup(request)));
    }

    // 일반 로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginDto.Response>> login(@RequestBody @Validated LoginDto.Request request) {
        return ResponseEntity.ok(BaseResponse.create(loginUseCase.login(request)));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<LoginDto.Response>> reissue(@RequestHeader(value = "X-REFRESH-TOKEN") String token) {
        return ResponseEntity.ok(BaseResponse.create(reissueUseCase.regenerateToken(token)));
    }
}
