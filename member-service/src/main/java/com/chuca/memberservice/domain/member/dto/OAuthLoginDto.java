package com.chuca.memberservice.domain.member.dto;

import lombok.*;

public class OAuthLoginDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String email;
        private String nickname;
        private String profileImage;
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
