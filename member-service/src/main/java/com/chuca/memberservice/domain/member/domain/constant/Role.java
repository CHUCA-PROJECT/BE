package com.chuca.memberservice.domain.member.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    OWNER("ROLE_OWNER"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    // Enum의 ROLE 조회
    public static String getRole(String role){
        return Role.valueOf(role).getRole();
    }
}
