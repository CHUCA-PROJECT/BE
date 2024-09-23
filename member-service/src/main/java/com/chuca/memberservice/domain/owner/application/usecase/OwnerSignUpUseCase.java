package com.chuca.memberservice.domain.owner.application.usecase;

import com.chuca.memberservice.domain.owner.application.dto.BankDto;
import com.chuca.memberservice.domain.owner.application.dto.CafeDto;
import com.chuca.memberservice.domain.owner.application.dto.OwnerDto;
import com.chuca.memberservice.domain.owner.domain.entity.Cafe;
import com.chuca.memberservice.domain.owner.domain.entity.Owner;
import com.chuca.memberservice.domain.owner.domain.service.OwnerService;
import com.chuca.memberservice.global.annotation.UseCase;
import com.chuca.memberservice.global.dto.BusinessDto;
import com.chuca.memberservice.global.feign.BusinessManClient;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@UseCase
@Transactional
@RequiredArgsConstructor
public class OwnerSignUpUseCase {
    @Value("${business.validate.key}")
    private String serviceKey;

    private final OwnerService ownerService;
    private final JwtProvider jwtProvider;
    private final BusinessManClient businessManClient;

    // 회원가입 및 입점 신청
    public CafeDto.Response signup(OwnerDto.Request request) {
        Owner owner = ownerService.signup(request); // 1. 사장님 DB에 저장
        Cafe cafe = ownerService.enrollCafe(owner, request.getCafeDto()); // 2. 카페 DB에 저장
        String accessToken = jwtProvider.encodeJwtToken(owner.getId(), owner.getRole()); // 2. access token 발급
        String refreshToken = jwtProvider.encodeJwtRefreshToken(owner.getId(), owner.getRole()); // 3. refresh token 발급
        jwtProvider.storeJwtRefreshToken(owner.getId(), owner.getRole(), refreshToken); // 4. redis에 refresh token 저장

        return new CafeDto.Response(cafe, accessToken, refreshToken);
    }

    // 사업자 진위 여부 확인
    public Boolean checkBusinessNum(BusinessDto.Request request) {
        BusinessDto.Response response = businessManClient.checkValidate(serviceKey, request);
        BusinessDto.ValidData data = response.getData().get(0); // 요청을 List로 보내지만 실질적 데이터는 1개
        String valid = data.getValid(); // 01 : valid, 02 : invalid
        return valid.equals("01");
    }

//    // 계좌 실명 조회
//    public String checkAccountRealName(BankDto request) {
//        BankDto ownerService.checkAccountRealName(request);
//    }
}
