package com.chuca.memberservice.global.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailDto {
    @Getter
    public static class Request {
        @Email(message = "이메일을 올바른 형식으로 입력해주세요.")
        @NotEmpty(message = "이메일을 입력해주세요.")
        private String email;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String code;
    }
}
