package com.chuca.memberservice.domain.member.service;

import com.chuca.memberservice.domain.member.repository.MemberRepository;
import com.chuca.memberservice.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberDetailServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        System.out.println("로그인한 memberId : " + memberId);
        UserDetails result = (UserDetails) memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new BadRequestException("해당하는 사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
        log.info("UserDetails: " + result.toString());

        return result;
    }
}
