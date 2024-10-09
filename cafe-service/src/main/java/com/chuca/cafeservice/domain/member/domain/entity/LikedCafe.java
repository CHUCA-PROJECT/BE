package com.chuca.cafeservice.domain.member.domain.entity;

import com.chuca.cafeservice.global.entity.BaseTime;
import com.chuca.cafeservice.global.entity.Cafe;
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
@Table(name = "likedCafe")
public class LikedCafe extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likedCafeId")
    private Long id;

    // 카페
    @ManyToOne
    @JoinColumn(name="cafeId")
    private Cafe cafe;
}
