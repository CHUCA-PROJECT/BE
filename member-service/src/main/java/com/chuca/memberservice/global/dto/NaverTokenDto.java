package com.chuca.memberservice.global.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class NaverTokenDto {
    private String access_token; //
    private String refresh_token;
    private String token_type; // 토큰 타입 (Bearer / MAC)
    private Integer expires_in; // 유효기간 (초단위)
    private String error; // 에러 코드
    private String error_description; // 에러 메시지
}
