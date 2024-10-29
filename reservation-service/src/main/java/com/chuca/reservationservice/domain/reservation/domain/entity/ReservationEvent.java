package com.chuca.reservationservice.domain.reservation.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationEvent {

    private Long reservationId;
    private Long cafeId;
    private EventType eventType; // 예약 이벤트 타입 (예: 생성, 취소, 승인 등)
    private String message;

    public enum EventType {
        CREATE, CANCEL, APPROVE, REJECT
    }
}