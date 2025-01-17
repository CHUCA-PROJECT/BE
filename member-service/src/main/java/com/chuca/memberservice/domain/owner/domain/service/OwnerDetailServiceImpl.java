package com.chuca.memberservice.domain.owner.domain.service;

import com.chuca.memberservice.domain.member.domain.repository.MemberRepository;
import com.chuca.memberservice.domain.owner.domain.repository.OwnerRepository;
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
public class OwnerDetailServiceImpl implements UserDetailsService {
    private final OwnerRepository ownerRepository;

    @Override
    public UserDetails loadUserByUsername(String ownerId) throws UsernameNotFoundException {
        System.out.println("로그인한 ownerId : " + ownerId);
        UserDetails result = (UserDetails) ownerRepository.findById(Long.parseLong(ownerId))
                .orElseThrow(() -> new BadRequestException("해당하는 사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
        return result;
    }

}