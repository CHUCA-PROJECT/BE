package com.chuca.memberservice.domain.owner.dto;

import com.chuca.memberservice.domain.owner.domain.constant.Bank;
import com.chuca.memberservice.global.annotation.Enum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

public class OwnerDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Request {
        @Email
        private String email; // 이메일

        @NotBlank(message = "사업자 등록 번호를 입력해주세요.")
        private String businessNum; // 사업자 등록 번호

        @NotBlank(message = "사업자 등록번호 사본를 첨부해주세요")
        private String businessImage; // 사업자 등록번호 사본

        @NotBlank(message = "개업일자를 입력해주세요.")
        private LocalDate openingDate; // 개업일자

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 영문자, 숫자, 특수문자 조합을 입력해야합니다.")
        private String password; // 비밀번호

        @NotBlank(message = "사업주명을 입력해주세요.")
        private String name; // 사업주명

        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        private String phone; // 사업자 휴대폰 번호

        @NotBlank(message = "계좌번호를 입력해주세요.")
        private String account; // 계좌번호

        @Enum(enumClass = Bank.class)
        private Bank bank; // 은행

        @NotNull
        private boolean agreeOption; // 선택 약관 동의

        @NotBlank(message = "닉네임을 입력해주세요.")
        private String nickname;

        private String profileImage;

        @NotNull
        private CafeDto.Request cafeDto; // 카페 정보
    }
}
