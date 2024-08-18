package com.chuca.memberservice.global.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessDto {
    @Getter
    public static class Request {
        List<Business> businesses;
    }

    @Getter
    public static class Business {
        @NotBlank
        private String b_no; // 사업자 등록 번호
        @NotBlank
        private String start_dt; // 개업일자
        @NotBlank
        private String p_nm; // 사업자명
    }

    @Getter
    public static class Response {
        private String status_code; // API 상태 코드
        private Integer request_cnt; // 조회 요청 수
        private Integer valid_cnt; // 검증 valid 수
        private List<ValidData> data; // 사업자 등록 정보 진위확인 결과
    }

    @Getter
    public static class ValidData {
        private String b_no; // 사업자 등록 번호
        private String valid; // 진위여부 (01 : valid, 02 : invalid)
        private Object request_param; // 사업자등록정보 진위확인
    }
}
