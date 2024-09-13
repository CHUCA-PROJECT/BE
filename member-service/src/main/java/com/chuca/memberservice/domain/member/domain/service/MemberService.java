package com.chuca.memberservice.domain.member.domain.service;

import com.chuca.memberservice.domain.member.application.dto.CheckDto;
import com.chuca.memberservice.domain.member.application.dto.LoginDto;
import com.chuca.memberservice.domain.member.application.dto.SignUpDto;
import com.chuca.memberservice.domain.member.domain.constant.Role;
import com.chuca.memberservice.domain.member.domain.entity.Member;
import com.chuca.memberservice.domain.member.domain.repository.MemberRepository;
import com.chuca.memberservice.global.exception.BadRequestException;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
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
    public Member signup(SignUpDto.Request request) {
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
                        request.getProfileImage(),
                        Role.USER
                )
        );

        return member;
    }

    // 아이디 중복 확인
    public boolean checkId(String generalId) {
        Optional<Member> member = memberRepository.findByGeneralId(generalId);
        if(member.isPresent())
            return false;    // 이미 존재하기에 사용 불가능
        return true;
    }

    // 닉네임 중복 확인
    public boolean checkNickname(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        if(member.isPresent())
            return false;    // 이미 존재하기에 사용 불가능
        return true;
    }

    // 회원 조회
    public Member getMember(String generalId) {
        Optional<Member> getMember = memberRepository.findByGeneralId(generalId);
        if(getMember.isEmpty())
            throw new BadRequestException("잘못된 아이디 입니다.", HttpStatus.BAD_REQUEST);
        return getMember.get();
    }

    // 비밀번호 체크
    public boolean checkPassword(Member member, String password) {
        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        return true;
    }

    // 로그아웃
    public boolean logout(String token) {
        Map<String, Object> memberInfo = jwtProvider.getMemberInfoFromJwtToken(token);
        Long memberId = (Long) memberInfo.get("memberId");
        Role role = (Role) memberInfo.get("role");
        jwtProvider.expireToken(memberId, role, token);
        return true;
    }
}
