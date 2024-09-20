package com.chuca.memberservice.domain.owner.presentation;

import com.chuca.memberservice.domain.owner.application.dto.CafeDto;
import com.chuca.memberservice.domain.owner.application.dto.LoginDto;
import com.chuca.memberservice.domain.owner.application.dto.OwnerDto;
import com.chuca.memberservice.domain.owner.application.usecase.OwnerLoginUseCase;
import com.chuca.memberservice.domain.owner.application.usecase.OwnerReissueUseCase;
import com.chuca.memberservice.domain.owner.application.usecase.OwnerSignUpUseCase;
import com.chuca.memberservice.global.dto.BusinessDto;
import com.chuca.memberservice.global.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {
    private final OwnerSignUpUseCase ownerSignUpUseCase;
    private final OwnerLoginUseCase ownerLoginUseCase;
    private final OwnerReissueUseCase ownerReissueUseCase;

    // 회원가입 및 입점신청
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<CafeDto.Response>> signup(@RequestBody @Validated OwnerDto.Request request) {
        return ResponseEntity.ok(BaseResponse.create(ownerSignUpUseCase.signup(request)));
    }

    // 사업자 진위 여부 확인
    @PostMapping("/check")
    public ResponseEntity<BaseResponse<Boolean>> checkBusinessNum(@RequestBody @Validated BusinessDto.Request request) {
        return ResponseEntity.ok(BaseResponse.create(ownerSignUpUseCase.checkBusinessNum(request)));
    }
//
//    // 3. 계좌 실명 조회
//    @PostMapping("/bank")
//    public ResponseEntity<BaseResponse<String>> checkAccountRealName(@RequestBody @Validated BankDto request) {
//        return ResponseEntity.ok(BaseResponse.create(ownerService.checkAccountRealName(request)));
//    }
//
    // 사장님 로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginDto.Response>> login(@RequestBody @Validated LoginDto.Request request) {
        return ResponseEntity.ok(BaseResponse.create(ownerLoginUseCase.login(request)));
    }

    // 로그아웃 (refresh token 유효하다는 전제 하) : 필터 거쳐야 함
    // 요청 보냈는데 access token 만료 -> 재발급
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Boolean>> logout(HttpServletRequest request) {
        return ResponseEntity.ok(BaseResponse.create(ownerLoginUseCase.logout(request)));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<com.chuca.memberservice.domain.member.application.dto.LoginDto.Response>> reissue(@RequestHeader(value = "X-REFRESH-TOKEN") String token) {
        return ResponseEntity.ok(BaseResponse.create(ownerReissueUseCase.regenerateToken(token)));
    }
}
