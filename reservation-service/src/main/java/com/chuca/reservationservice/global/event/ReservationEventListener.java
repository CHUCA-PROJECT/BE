package com.chuca.reservationservice.global.event;

import com.chuca.reservationservice.domain.reservation.domain.entity.ReservationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventListener {

    @EventListener
    public void handleReservationEvent(ReservationEvent event) {
        switch (event.getEventType()) {
            case CREATE:
                System.out.println("예약 생성 이벤트: " + event.getMessage());
                // 예약 생성에 따른 후속 작업 수행
                break;

            case CANCEL:
                System.out.println("예약 취소 이벤트: " + event.getMessage());
                // 예약 취소에 따른 후속 작업 수행
                break;

            case APPROVE:
                System.out.println("예약 승인 이벤트: " + event.getMessage());
                // 예약 승인에 따른 후속 작업 수행
                break;

            case REJECT:
                System.out.println("예약 거절 이벤트: " + event.getMessage());
                // 예약 거절에 따른 후속 작업 수행
                break;
        }
    }
}
