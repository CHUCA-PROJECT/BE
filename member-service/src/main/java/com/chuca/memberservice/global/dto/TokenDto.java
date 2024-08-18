package com.chuca.memberservice.global.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {
    @Getter
    @Builder
    public static class Request {
        private String imp_key;
        private String imp_secret;
    }

    @Getter
    public static class Response {
        private Integer code; // 응답 코드
        private String message; // 응답 메시지
        private AuthAnnotation response;

        @Getter
        public static class AuthAnnotation {
            private String access_token; // access_token
            private Integer expired_at; // token 만료 시각
            private Integer now; // 현재 시각
        }
    }
}
