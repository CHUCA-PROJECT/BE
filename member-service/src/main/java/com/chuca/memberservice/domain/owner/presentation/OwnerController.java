package com.chuca.memberservice.domain.owner.presentation;

import com.chuca.memberservice.domain.owner.application.usecase.OwnerLoginUseCase;
import com.chuca.memberservice.global.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {
    private final OwnerLoginUseCase ownerLoginUseCase;

    // 로그아웃 (refresh token 유효하다는 전제 하) : 필터 거쳐야 함
    // 요청 보냈는데 access token 만료 -> 재발급
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Boolean>> logout(HttpServletRequest request) {
        return ResponseEntity.ok(BaseResponse.create(ownerLoginUseCase.logout(request)));
    }
}
