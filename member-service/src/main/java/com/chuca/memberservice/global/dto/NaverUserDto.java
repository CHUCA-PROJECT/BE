package com.chuca.memberservice.global.dto;

import lombok.Getter;

@Getter
public class NaverUserDto {
    private String resultcode;
    private String message;
    private UserInfo response;

    @Getter
    public static class UserInfo {
        private String id; // 고유 식별값 (동일인 식별 정보)
        private String nickname; // 닉네임
        private String email; // 이메일
        private String profile_image; // 프로핊 이미지
        private String mobile; // 휴대폰 번호
    }
}
