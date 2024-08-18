package com.chuca.memberservice.domain.member.service;

import com.chuca.memberservice.domain.member.dto.CheckDto;
import com.chuca.memberservice.domain.member.dto.LoginDto;
import com.chuca.memberservice.domain.member.dto.SignUpDto;
import com.chuca.memberservice.domain.member.entity.Member;
import com.chuca.memberservice.domain.member.repository.MemberRepository;
import com.chuca.memberservice.global.exception.BadRequestException;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 일반 회원가입
    public SignUpDto.Response signup(SignUpDto.Request request) {
        if(!checkId(request.getGeneralId()).isCheck()) {
            throw new BadRequestException("이미 가입한 아이디입니다.", HttpStatus.BAD_REQUEST);
        }

        if(!checkNickname(request.getNickname()).isCheck()) {
            throw new BadRequestException("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }

        // 추후에 이미지 처리 해야함
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = memberRepository.save(
                new Member(
                        request.getGeneralId(),
                        encodedPassword,
                        request.getPhone(),
                        request.isLocTos(),
                        request.isAdTos(),
                        request.getNickname(),
                        request.getProfileImage()
                )
        );

        String accessToken = jwtProvider.encodeJwtToken(member.getId()); // 액세스 토큰 발급
        String refreshToken = jwtProvider.encodeJwtRefreshToken(member.getId()); // 리프레시 토큰 발급
        jwtProvider.storeJwtRefreshToken(member.getId(), refreshToken); // 리프레시 redis에 저장

        return new SignUpDto.Response(accessToken, refreshToken);
    }

    // 아이디 중복 확인
    public CheckDto.Response checkId(String generalId) {
        Optional<Member> member = memberRepository.findByGeneralId(generalId);
        if(member.isPresent())
            return new CheckDto.Response(false);    // 이미 존재하기에 사용 불가능
        return new CheckDto.Response(true);
    }

    // 닉네임 중복 확인
    public CheckDto.Response checkNickname(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        if(member.isPresent())
            return new CheckDto.Response(false);    // 이미 존재하기에 사용 불가능
        return new CheckDto.Response(true);
    }

    // 로그인
    public LoginDto.Response login(LoginDto.Request request) {
        Optional<Member> getMember = memberRepository.findByGeneralId(request.getGeneralId());

        if(getMember.isEmpty())
            throw new BadRequestException("잘못된 아이디 입니다.", HttpStatus.BAD_REQUEST);

        Member member = getMember.get();
        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String accessToken = jwtProvider.encodeJwtToken(member.getId()); // 액세스 토큰 발급
        String refreshToken = jwtProvider.encodeJwtRefreshToken(member.getId()); // 리프레시 토큰 발급
        jwtProvider.storeJwtRefreshToken(member.getId(), refreshToken); // 리프레시 redis에 저장

        return new LoginDto.Response(accessToken, refreshToken);
    }

    // 토큰 재발급
    public LoginDto.Response regenerateToken(String token) {
        if(token != null && jwtProvider.validateToken(token)) {
            return jwtProvider.regenerateToken(token);
        }
        throw new BadRequestException("RefreshToken이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
