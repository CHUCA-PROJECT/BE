package com.chuca.memberservice.global.dto;

import lombok.Getter;

@Getter
public class JwtTokenDto {

    private final Long memberId;

    public JwtTokenDto(Long memberId) {
        this.memberId = memberId;
    }
}
