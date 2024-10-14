package com.chuca.memberservice.domain.member.application.usecase;

import com.chuca.memberservice.domain.member.application.dto.ProfileDto;
import com.chuca.memberservice.domain.member.domain.entity.Member;
import com.chuca.memberservice.global.annotation.UseCase;

@UseCase
public class ProfileUseCase {
    // 프로필 조회
    public ProfileDto getProfile(Member member) {
        return ProfileDto.builder()
                .memberId(member.getId())
                .generalId(member.getGeneralId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .build();
    }
}
