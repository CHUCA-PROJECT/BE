package com.chuca.memberservice.domain.owner.application.usecase;

import com.chuca.memberservice.domain.owner.application.dto.CafeDto;
import com.chuca.memberservice.domain.owner.application.dto.OwnerDto;
import com.chuca.memberservice.domain.owner.domain.entity.Cafe;
import com.chuca.memberservice.domain.owner.domain.entity.Owner;
import com.chuca.memberservice.domain.owner.domain.service.OwnerService;
import com.chuca.memberservice.global.annotation.UseCase;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class OwnerSignUpUseCase {
    private final OwnerService ownerService;
    private final JwtProvider jwtProvider;

    // 회원가입 및 입점 신청
    public CafeDto.Response signup(OwnerDto.Request request) {
        Owner owner = ownerService.signup(request); // 1. 사장님 DB에 저장
        Cafe cafe = ownerService.enrollCafe(owner, request.getCafeDto()); // 2. 카페 DB에 저장
        String accessToken = jwtProvider.encodeJwtToken(owner.getId(), owner.getRole()); // 2. access token 발급
        String refreshToken = jwtProvider.encodeJwtRefreshToken(owner.getId(), owner.getRole()); // 3. refresh token 발급
        jwtProvider.storeJwtRefreshToken(owner.getId(), owner.getRole(), refreshToken); // 4. redis에 refresh token 저장

        return new CafeDto.Response(cafe, accessToken, refreshToken);
    }
}
