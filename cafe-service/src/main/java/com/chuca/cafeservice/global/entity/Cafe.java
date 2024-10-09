package com.chuca.cafeservice.global.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "cafe")
public class Cafe extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafeId")
    private Long id;

    @Column(name = "cafeName", nullable = false)
    private String name; // 카페명

    @Column(nullable = false)
    private String phone; // 카페 매장 전화번호

    @Column(name = "cafeDesc")
    private String description; // 카페 소개

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private Double latitude; // 위도

    @Column(nullable = false)
    private Double longitude; // 경도

    // 카페 보증금
    @Column(name = "deposit")
    @ColumnDefault("0") //default 0
    private int deposit;

    // 별점
    @Column(name = "rating")
    @ColumnDefault("0.0") //default 0
    private Double rating;
}