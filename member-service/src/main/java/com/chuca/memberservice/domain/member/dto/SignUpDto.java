package com.chuca.memberservice.domain.member.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

public class SignUpDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotEmpty
        private String generalId;

        @NotEmpty
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "비밀번호는 8자 이상 대문자, 소문자, 숫자, 특수문자 등 3가지 이상을 사용해 주세요.")
        private String password;

        @NotEmpty
        private String phone;

        @NotNull
        private boolean locTos;     // 위치 기반 서비스 이용약관 동의 여부

        @NotNull
        private boolean adTos;       // 광고성 정보 수신 동의 여부

        @NotEmpty
        private String nickname;

        private String profileImage;    // 추후에 이미지 관련 처리해야함
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String accessToken;
        private String refreshToken;
    }
}
