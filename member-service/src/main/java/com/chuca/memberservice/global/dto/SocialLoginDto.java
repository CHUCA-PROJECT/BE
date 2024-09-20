package com.chuca.memberservice.global.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLoginDto {
    @Getter
    public static class Request {
        // 소셜로부터 제공받은 유저 정보 (Naver, Kakao, Google)
        @NotNull
        private String email;
        @NotNull
        private String nickname;
        private String profileImg;
        private String targetId; // 카카오만 존재 (for. 회원탈퇴)

        // 동의 옵션
        private boolean locTos;     // 위치 기반 서비스 이용약관 동의 여부
        private boolean adTos;       // 광고성 정보 수신 동의 여부
    }
}
