package com.chuca.memberservice.domain.member.presentation;

import com.chuca.memberservice.domain.member.application.dto.ProfileDto;
import com.chuca.memberservice.domain.member.application.usecase.LoginUseCase;
import com.chuca.memberservice.domain.member.application.usecase.ProfileUseCase;
import com.chuca.memberservice.domain.member.domain.entity.Member;
import com.chuca.memberservice.global.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final LoginUseCase loginUseCase;
    private final ProfileUseCase profileUseCase;

    // 로그아웃 (refresh token 유효하다는 전제 하) : 필터 거쳐야 함
    // 요청 보냈는데 access token 만료 -> 재발급
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Boolean>> logout(HttpServletRequest request) {
        return ResponseEntity.ok(BaseResponse.create(loginUseCase.logout(request)));
    }

    // 테스트용
    @GetMapping("/test-gateway")
    public ResponseEntity<BaseResponse<String>> testGateway(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(BaseResponse.create(member.getGeneralId()));
    }

    // 타 서비스 Member 정보 주입용
    @GetMapping("/profile")
    public ResponseEntity<BaseResponse<ProfileDto>> profile(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(BaseResponse.create(profileUseCase.getProfile(member)));
    }
}
