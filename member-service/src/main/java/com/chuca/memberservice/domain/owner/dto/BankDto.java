package com.chuca.memberservice.domain.owner.dto;

import com.chuca.memberservice.domain.domain.constant.Bank;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BankDto {
    @NotBlank
    private Bank bank_code; // 은행코드
    @NotBlank
    private String bank_num; // 계좌번호
}
