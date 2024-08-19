package com.chuca.memberservice.domain.owner.domain.entity;

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

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "owner")
public class Owner extends BaseTime {
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

    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
    private List<Cafe> cafes;


    @Builder
    public Owner(String email, String businessNum, String businessImage, LocalDate openingDate, String password,
                 String name, String phone, String account, Bank bank, boolean agreeOption, String nickname, String profileImage) {
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
    }
}
