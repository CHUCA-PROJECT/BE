package com.chuca.memberservice.domain.owner.entity;

import com.chuca.memberservice.domain.owner.dto.CafeDto;
import com.chuca.memberservice.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(nullable = false)
    private String name; // 카페명

    @Column(nullable = false)
    private String phone; // 카페 전화번호

    private String description; // 카페 소개

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private Double latitude; // 위도

    @Column(nullable = false)
    private Double longitude; // 경도

    @ManyToOne
    @JoinColumn(name="ownerId")
    private Owner owner;

    @Builder
    public Cafe(CafeDto.Request request, Owner owner) {
        this.name = request.getName();
        this.phone = request.getPhone();
        this.address = request.getAddress();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
        this.owner = owner;
    }
}
