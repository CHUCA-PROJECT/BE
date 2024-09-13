package com.chuca.memberservice.domain.owner.domain.entity;

import com.chuca.memberservice.domain.member.domain.constant.Role;
import com.chuca.memberservice.domain.owner.domain.constant.Bank;
import com.chuca.memberservice.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "owner")
public class Owner extends BaseTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ownerId")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false, unique = true)
    private String businessNum; // 사업자 등록 번호

    @Column(nullable = false)
    private String businessImage; // 사업자 등록번호 사본

    @Column(nullable = false)
    private LocalDate openingDate; // 개업일자

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String name; // 사업주명

    @Column(nullable = false)
    private String phone; // 사업자 휴대폰 번호

    @Column(nullable = false, unique = true)
    private String account; // 계좌번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Bank bank; // 은행

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean agreeOption; // 선택 약관 동의

    @Column(nullable = false)
    private String nickname;

    private String profileImage; // 프로필 이미지

    @Column(nullable = false)
    @ColumnDefault("'active'")
    private String status; // 상태

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
    private List<Cafe> cafes;


    @Builder
    public Owner(String email, String businessNum, String businessImage, LocalDate openingDate, String password,
                 String name, String phone, String account, Bank bank, boolean agreeOption, String nickname, String profileImage, Role role) {
        this.email = email;
        this.businessNum = businessNum;
        this.businessImage = businessImage;
        this.openingDate = openingDate;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.account = account;
        this.bank = bank;
        this.agreeOption = agreeOption;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;
    }

    /**** UserDetails 오버라이딩 메소드 ****/

    /*
     * 유저의 권한 반환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
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
