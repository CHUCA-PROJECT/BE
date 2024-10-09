package com.chuca.cafeservice.global.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "menu")
public class Menu extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menuId")
    private Long id;

    @Column(name = "menuName", nullable = false)
    private String name; // 메뉴명

    @Column(name = "price", nullable = false)
    private int price; // 가격

    @Column(name = "menuDesc", nullable = false)
    private String description; // 메뉴 설명

    @Lob
    private String imageUrl; // 메뉴 사진

    @Column(name = "menuSequence", nullable = false)
    private String menuSequence; // 메뉴 순서

    // 카페
    @ManyToOne
    @JoinColumn(name="cafeId")
    private Cafe cafe;
}
