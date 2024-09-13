package com.chuca.memberservice.domain.owner.application.dto;

import com.chuca.memberservice.domain.owner.domain.entity.Cafe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class CafeDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "카페명을 입력해주세요.")
        private String name;
        @NotBlank(message = "카페 전화번호를 입력해주세요.")
        private String phone;
        @NotBlank(message = "주소를 입력해주세요.")
        private String address;
        @NotNull
        private Double latitude;
        @NotNull
        private Double longitude;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long cafeId;
        private String name;
        private String status;
        private String accessToken;
        private String refreshToken;

        public Response(Cafe cafe, String accessToken, String refreshToken) {
            this.cafeId = cafe.getId();
            this.name = cafe.getName();
            this.status = "입점 신청이 완료되었습니다.";
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
