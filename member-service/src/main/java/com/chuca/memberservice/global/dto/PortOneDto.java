package com.chuca.memberservice.global.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortOneDto {
    private Integer code; // 응답코드
    private String message; // 응답메시지
    private VbankHolderAnnotation response;

    @Getter
    public static class VbankHolderAnnotation {
        private String bank_holder; // 계좌의 예금주명
    }
}
