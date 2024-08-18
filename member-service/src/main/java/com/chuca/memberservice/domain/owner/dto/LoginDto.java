package com.chuca.memberservice.domain.owner.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class LoginDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String businessNum;
        @NotBlank
        private String password;
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
