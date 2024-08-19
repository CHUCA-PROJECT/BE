package com.chuca.memberservice.domain.member.domain.service.oauth;

import com.chuca.memberservice.domain.member.domain.constant.MemberProvider;
import com.chuca.memberservice.domain.member.application.dto.OAuthLoginDto;
import com.chuca.memberservice.domain.member.domain.entity.Member;
import com.chuca.memberservice.domain.member.domain.repository.MemberRepository;
import com.chuca.memberservice.global.exception.BadRequestException;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoOauthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    // 카카오
    // 로그인 및 회원가입
    public OAuthLoginDto.Response kakaoLogin(OAuthLoginDto.Request kakaoReqDto) {
        String email = kakaoReqDto.getEmail();
        log.info(kakaoReqDto.getEmail());

        if(email == null)
            throw new BadRequestException("이메일 정보가 비어있습니다.", HttpStatus.BAD_REQUEST);

        Member getMember = memberRepository.findByEmail(email);
        Member member;
        if(getMember != null){  // 이미 회원가입한 회원인 경우
            member = getMember;
            if(!member.getProvider().equals(MemberProvider.KAKAO))   // 이미 회원가입했지만 Kakao가 아닌 다른 소셜 로그인 사용
                throw new BadRequestException("이미 다른 경로로 가입한 회원입니다.", HttpStatus.UNAUTHORIZED);
        } else {    // 아직 회원가입 하지 않은 회원인 경우
            member = memberRepository.save(
                    Member.builder()
                            .email(email)
                            .nickname(kakaoReqDto.getNickname())
                            .profileImage(kakaoReqDto.getProfileImage())
                            .provider(MemberProvider.KAKAO)
                            .build()
            );
            System.out.println("member id : " + member.getId());
            System.out.println("member email : " + member.getEmail());
            System.out.println("member nickname : " + member.getNickname());
        }

        // accessToken, refreshToken 발급 후 반환
        return createToken(member); // 임시
    }

    // accessToken, refreshToken 발급
    @Transactional
    public OAuthLoginDto.Response createToken(Member member) {
        String newAccessToken = jwtProvider.encodeJwtToken(member.getId());
        String newRefreshToken = jwtProvider.encodeJwtRefreshToken(member.getId());

        System.out.println("newAccessToken : " + newAccessToken);
        System.out.println("newRefreshToken : " + newRefreshToken);

        // DB에 refreshToken 저장
//        member.updateRefreshToken(newRefreshToken);
        memberRepository.save(member);

        System.out.println("member nickname : " + member.getNickname());

        return new OAuthLoginDto.Response(newAccessToken, newRefreshToken);
    }

    // refreshToken으로 accessToken 발급하기
    @Transactional
    public OAuthLoginDto.Response regenerateAccessToken(String accessToken, String refreshToken) {
        if(jwtProvider.validateToken(accessToken))  // access token 유효성 검사
            throw new BadRequestException("유효한 Access Token입니다.", HttpStatus.BAD_REQUEST);

        if(!jwtProvider.validateToken(refreshToken))  // refresh token 유효성 검사
            throw new BadRequestException("유효하지 않은 Refresh Token입니다. 다시 로그인하세요.", HttpStatus.UNAUTHORIZED);

        Long memberId = jwtProvider.getMemberIdFromJwtToken(refreshToken);
        log.info("memberId : " + memberId);

        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getMember.isEmpty())
            throw new BadRequestException("해당하는 사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);

        Member member = getMember.get();
//        if(!refreshToken.equals(member.getRefreshToken()))
//            throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);

        String newRefreshToken = jwtProvider.encodeJwtRefreshToken(memberId);
        String newAccessToken = jwtProvider.encodeJwtToken(memberId);

//        member.updateRefreshToken(newRefreshToken);
        memberRepository.save(member);

        System.out.println("member nickname : " + member.getNickname());

        return new OAuthLoginDto.Response(newAccessToken, newRefreshToken);
    }
}
