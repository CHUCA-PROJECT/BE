package com.chuca.memberservice.domain.member.presentation;

import com.chuca.memberservice.domain.member.application.dto.CheckDto;
import com.chuca.memberservice.domain.member.application.dto.LoginDto;
import com.chuca.memberservice.domain.member.application.dto.SignUpDto;
import com.chuca.memberservice.domain.member.application.usecase.LoginUseCase;
import com.chuca.memberservice.domain.member.application.usecase.ReissueUseCase;
import com.chuca.memberservice.domain.member.application.usecase.SignUpUseCase;
import com.chuca.memberservice.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final SignUpUseCase signUpUseCase;
    private final LoginUseCase loginUseCase;
    private final ReissueUseCase reissueUseCase;

    // 휴대전화 인증


    // 아이디 중복 확인
    @GetMapping("/check-id")
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

    // 로그아웃 (refresh token 유효하다는 전제 하) : 필터 거쳐야 함
    // 요청 보냈는데 access token 만료 -> 재발급
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Boolean>> logout(@RequestHeader(value = "Authentication") String token) {
        return ResponseEntity.ok(BaseResponse.create(loginUseCase.logout(token)));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<LoginDto.Response>> reissue(@RequestHeader(value = "X-REFRESH-TOKEN") String token) {
        return ResponseEntity.ok(BaseResponse.create(reissueUseCase.regenerateToken(token)));
    }
}
