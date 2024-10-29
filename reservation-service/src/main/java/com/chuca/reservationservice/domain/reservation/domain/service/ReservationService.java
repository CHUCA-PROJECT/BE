package com.chuca.reservationservice.domain.reservation.domain.service;
import com.chuca.reservationservice.domain.reservation.application.dto.ReservationRequestDTO;
import com.chuca.reservationservice.domain.reservation.application.dto.ReservationResponseDTO;
import com.chuca.reservationservice.domain.reservation.domain.entity.Reservation;
import com.chuca.reservationservice.domain.reservation.domain.entity.ReservationEvent;
import com.chuca.reservationservice.domain.reservation.domain.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ReservationRepository reservationRepository;

    // 예약 슬롯 확인 및 설정
    public boolean checkAndReserveSlot(Long cafeId, LocalDate date) {
        String key = "cafe:" + cafeId + ":date:" + date.toString();

        if (redisTemplate.opsForValue().get(key) != null) {
            return false; // 이미 예약된 슬롯
        }

        redisTemplate.opsForValue().set(key, "reserved"); // 예약 슬롯 등록
        return true;
    }

    // 예약 생성
    public void createReservation(ReservationRequestDTO request) {
        Reservation reservation = new Reservation();

        // DTO 데이터를 Reservation 엔티티에 매핑
        reservation.setReservationDate(request.getReservationDate());
        reservation.setPeriodDate(request.getPeriodDate());
        reservation.setPrePeriodDate(request.getPrePeriodDate());
        reservation.setDemolishDate(request.getDemolishDate());
        reservation.setBusinessHours(request.getBusinessHours());
        reservation.setBirthDayName(request.getBirthDayName());
        reservation.setGenre(request.getGenre());
        reservation.setEventName(request.getEventName());
        reservation.setHeadCount(request.getHeadCount());
        reservation.setImgUrl(request.getImgUrl());
        reservation.setMemberId(request.getMemberId());
        reservation.setCafeId(request.getCafeId());

        reservationRepository.save(reservation);

        // 예약 생성 이벤트 발행
        ReservationEvent event = new ReservationEvent(reservation.getReservationId(), reservation.getCafeId(), ReservationEvent.EventType.CREATE, "예약이 생성되었습니다.");
        eventPublisher.publishEvent(event);
    }

    // 예약 상태 조회
    public ReservationResponseDTO getReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));
        return mapToDTO(reservation);
    }

    // 예약 취소
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        // Redis에서 슬롯 제거
        releaseSlot(reservation.getCafeId(), reservation.getReservationDate());
        reservationRepository.delete(reservation);

        // 예약 취소 이벤트 발행
        ReservationEvent event = new ReservationEvent(reservationId, reservation.getCafeId(), ReservationEvent.EventType.CANCEL, "예약이 취소되었습니다.");
        eventPublisher.publishEvent(event);
    }

    // 예약 승인
    public void approveReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));
        reservation.setReservationStatus(1); // 승인 상태로 변경
        reservationRepository.save(reservation);

        // 예약 승인 이벤트 발행
        ReservationEvent event = new ReservationEvent(reservationId, reservation.getCafeId(), ReservationEvent.EventType.APPROVE, "예약이 승인되었습니다.");
        eventPublisher.publishEvent(event);
    }

    // 예약 거절
    public void rejectReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));
        reservation.setReservationStatus(2); // 거절 상태로 변경
        reservationRepository.save(reservation);

        // 예약 거절 이벤트 발행
        ReservationEvent event = new ReservationEvent(reservationId, reservation.getCafeId(), ReservationEvent.EventType.REJECT, "예약이 거절되었습니다.");
        eventPublisher.publishEvent(event);
    }

    private ReservationResponseDTO mapToDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        // DTO 필드 매핑 코드
        return dto;
    }

    public void releaseSlot(Long cafeId, LocalDate date) {
        String key = "cafe:" + cafeId + ":date:" + date.toString();
        redisTemplate.delete(key);
    }
}