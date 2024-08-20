package com.chuca.memberservice.domain.member.domain.entity;

import com.chuca.memberservice.domain.member.domain.constant.MemberProvider;
import com.chuca.memberservice.domain.member.domain.constant.Role;
import com.chuca.memberservice.global.entity.BaseTime;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "member")
public class Member extends BaseTime implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    // 일반 로그인 시 필요한 아이디 (일반 로그인에만 사용)
    @Column(nullable = true, unique = true)
    private String generalId;

    @Column(unique = true)
    private String email; // 이메일 (소셜 로그인에만 사용)

    @Column(unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'APP'")
    private MemberProvider provider;

    private String phone;

    @Column(nullable = false)
    private boolean locTos;     // 위치 기반 서비스 이용약관 동의 여부

    @Column(nullable = false)
    private boolean adTos;       // 광고성 정보 수신 동의 여부

    @Column(nullable = false, unique = true)
    private String nickname;

    private String profileImage; // 프로필 이미지

    @Column(nullable = false)
    @ColumnDefault("'active'")
    private String status;

    @Type(JsonType.class)
    @Column(name = "alarms", columnDefinition = "longtext")
    private Map<String, Object> alarms = new LinkedHashMap<>();     // 알림 설정 (관심 키워드, 관심 카페, 연락, 야간 푸시 알림)\

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    // 소셜 로그인 시 활용
    @Builder
    public Member(String email, MemberProvider provider, String phone, String nickname, String profileImage, Role role) {
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("option1", true); // 관심 키워드
        settings.put("option2", true); // 관심 카페
        settings.put("option3", true); // 연락
        settings.put("option4", true); // 야간 푸시 알림

        this.email = email;
        this.provider = provider;
        this.phone = phone;
        this.nickname = nickname;
        this.profileImage = profileImage != null ? profileImage : "";
        this.locTos = false;
        this.adTos = false;
        this.alarms = settings;
        this.role = role;
    }

    // 일반 로그인 시 활용
    public Member(String gerneralId, String password, String phone, boolean locTos, boolean adTos, String nickname, String profileImage, Role role) {
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("option1", true); // 관심 키워드
        settings.put("option2", true); // 관심 카페
        settings.put("option3", true); // 연락
        settings.put("option4", true); // 야간 푸시 알림

        this.generalId = gerneralId;
        this.password = password;
        this.phone = phone;
        this.locTos = locTos;
        this.adTos = adTos;
        this.nickname = nickname;
        this.profileImage = profileImage != null ? profileImage : "";
        this.alarms = settings;
        this.role = role;
    }

    /**** UserDetails 오버라이딩 메소드 ****/

    /*
     * 유저의 권한 목록 반환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        // simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
        log.info("role.getRole() : {}", role.getRole());
        log.info("simpleGrantedAuthorities : {}", simpleGrantedAuthorities.get(0));
        return simpleGrantedAuthorities;
    }

    /*
     * 유저 Id(PK) 반환
     */
    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    /*
     * 계정 만료 여부
     * true : 만료 X
     * false : 만료 O
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /*
     * 계정 잠김 여부
     * true : 잠김 X
     * false : 잠김 O
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /*
     * 비밀번호 만료 여부
     * true : 만료 X
     * false : 만료 O
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /*
     * 사용자 활성화 여부
     * true : 활성화 O
     * false : 활성화 X
     */
    @Override
    public boolean isEnabled() {
        return false;
    }
}
