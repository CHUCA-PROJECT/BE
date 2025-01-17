package com.chuca.memberservice.domain.member.domain.repository;

import com.chuca.memberservice.domain.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Optional<Member> findByGeneralId(String generalId);
    Optional<Member> findByNickname(String nickname);
}
