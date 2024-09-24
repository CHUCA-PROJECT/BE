package com.chuca.memberservice.domain.owner.application.usecase;

import com.chuca.memberservice.domain.owner.application.dto.BankDto;
import com.chuca.memberservice.domain.owner.application.dto.CafeDto;
import com.chuca.memberservice.domain.owner.application.dto.OwnerDto;
import com.chuca.memberservice.domain.owner.domain.constant.Bank;
import com.chuca.memberservice.domain.owner.domain.entity.Cafe;
import com.chuca.memberservice.domain.owner.domain.entity.Owner;
import com.chuca.memberservice.domain.owner.domain.service.OwnerService;
import com.chuca.memberservice.global.annotation.UseCase;
import com.chuca.memberservice.global.dto.BusinessDto;
import com.chuca.memberservice.global.dto.PortOneDto;
import com.chuca.memberservice.global.dto.TokenDto;
import com.chuca.memberservice.global.exception.BadRequestException;
import com.chuca.memberservice.global.exception.UnauthorizedException;
import com.chuca.memberservice.global.feign.BusinessManClient;
import com.chuca.memberservice.global.feign.PortOneClient;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@UseCase
@Transactional
@RequiredArgsConstructor
public class OwnerSignUpUseCase {
    @Value("${business.validate.key}")
    private String serviceKey;
    @Value("${imp.key}")
    private String impKey;
    @Value("${imp.secret}")
    private String impSecret;

    private final OwnerService ownerService;
    private final JwtProvider jwtProvider;
    private final BusinessManClient businessManClient;
    private final PortOneClient portOneClient;

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

    // 계좌 실명 조회
    public String checkAccountRealName(BankDto request) {
        TokenDto.Request tokenRequest = TokenDto.Request.builder()
                .imp_key(impKey)
                .imp_secret(impSecret)
                .build();

        TokenDto.Response tokenResponse = portOneClient.getToken(tokenRequest);
        if(tokenResponse.getCode() != 0)
            throw new UnauthorizedException("Access token 발급에 실패했습니다.", HttpStatus.UNAUTHORIZED);

        Bank bank = request.getBank_code();
        String accessToken = "Bearer " + tokenResponse.getResponse().getAccess_token();
        PortOneDto portOneResponse = portOneClient.getBankHolder(accessToken, bank.getValue(), request.getBank_num());
        if(portOneResponse.getCode() != 0)
            throw new BadRequestException("계좌 정보 조회에 실패했습니다.", HttpStatus.UNAUTHORIZED);
        return portOneResponse.getResponse().getBank_holder(); // 계좌 예금주명
    }
}
