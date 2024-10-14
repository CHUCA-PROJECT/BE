package com.chuca.memberservice.domain.member.application.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProfileDto {
    private Long memberId;
    private String generalId;
    private String email;
    private String nickname;
    private String profileImage;
}
