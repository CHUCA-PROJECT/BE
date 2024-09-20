package com.chuca.memberservice.domain.owner.domain.service;

import com.chuca.memberservice.domain.member.domain.constant.Role;
import com.chuca.memberservice.domain.member.domain.entity.Member;
import com.chuca.memberservice.domain.owner.domain.constant.Bank;
import com.chuca.memberservice.domain.owner.application.dto.BankDto;
import com.chuca.memberservice.domain.owner.application.dto.CafeDto;
import com.chuca.memberservice.domain.owner.application.dto.LoginDto;
import com.chuca.memberservice.domain.owner.application.dto.OwnerDto;
import com.chuca.memberservice.domain.owner.domain.entity.Cafe;
import com.chuca.memberservice.domain.owner.domain.entity.Owner;
import com.chuca.memberservice.domain.owner.domain.repository.CafeRepository;
import com.chuca.memberservice.domain.owner.domain.repository.OwnerRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class OwnerService {
//    @Value("${imp.key}")
//    private String impKey;
//    @Value("${imp.secret}")
//    private String impSecret;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;
    private final CafeRepository cafeRepository;
    private final PortOneClient portOneClient;

    // 회원가입
    public Owner signup(OwnerDto.Request request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Owner newOwner = Owner.builder()
                .email(request.getEmail())
                .businessNum(request.getBusinessNum())
                .businessImage(request.getBusinessImage())
                .openingDate(request.getOpeningDate())
                .password(encodedPassword)
                .name(request.getName())
                .phone(request.getPhone())
                .account(request.getAccount())
                .bank(request.getBank())
                .agreeOption(request.isAgreeOption())
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .role(Role.OWNER)
                .build();
        return ownerRepository.save(newOwner);
    }

    // 카페 입점 신청 (등록)
    public Cafe enrollCafe(Owner owner, CafeDto.Request cafeDto) {
        Cafe newCafe = Cafe.builder()
                .request(cafeDto)
                .owner(owner)
                .build();
        return cafeRepository.save(newCafe);
    }

    // 사장님 조회
    public Owner getOwner(String businessNum) {
        Optional<Owner> owner = ownerRepository.findByBusinessNum(businessNum);
        if(owner.isEmpty()) {
            throw new BadRequestException("일치하는 회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        return owner.get();
    }

    // 비밀번호 체크
    public boolean checkPassword(Owner owner, String password) {
        if(!passwordEncoder.matches(password, owner.getPassword())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        return true;
    }
//
//    // 계좌 실명 확인
//    public String checkAccountRealName(BankDto request) {
//        TokenDto.Request tokenRequest = TokenDto.Request.builder()
//                .imp_key(impKey)
//                .imp_secret(impSecret)
//                .build();
//
//        TokenDto.Response tokenResponse = portOneClient.getToken(tokenRequest);
//        if(tokenResponse.getCode() != 0)
//            throw new UnauthorizedException("Access token 발급에 실패했습니다.", HttpStatus.UNAUTHORIZED);
//
//        Bank bank = request.getBank_code();
//        String accessToken = "Bearer " + tokenResponse.getResponse().getAccess_token();
//        PortOneDto portOneResponse = portOneClient.getBankHolder(accessToken, bank.getValue(), request.getBank_num());
//        if(portOneResponse.getCode() != 0)
//            throw new BadRequestException("계좌 정보 조회에 실패했습니다.", HttpStatus.UNAUTHORIZED);
//        return portOneResponse.getResponse().getBank_holder(); // 계좌 예금주명
//    }


    // 로그아웃
    public boolean logout(String token) {
        Map<String, Object> memberInfo = jwtProvider.getMemberInfoFromJwtToken(token);
        Long memberId = (Long) memberInfo.get("memberId");
        Role role = (Role) memberInfo.get("role");
        jwtProvider.expireToken(memberId, role, token);
        return true;
    }
}
