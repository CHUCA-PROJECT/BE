package com.chuca.reservationservice.domain.reservation.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalDate periodDate;

    private LocalDate prePeriodDate;
    private LocalDate demolishDate;
    private LocalDate businessHours;

    @Column(nullable = false)
    private int reservationStatus = 0; // 0: 대기중, 1: 승인, 2: 거절 등 상태 값 설정 가능

    private String birthDayName;
    private String genre;
    private String eventName;
    private Integer headCount;
    private String imgUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long cafeId;
}
