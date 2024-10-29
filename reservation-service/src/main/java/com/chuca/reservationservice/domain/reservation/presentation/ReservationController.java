package com.chuca.reservationservice.domain.reservation.presentation;

import com.chuca.reservationservice.domain.reservation.application.dto.ReservationRequestDTO;
import com.chuca.reservationservice.domain.reservation.application.dto.ReservationResponseDTO;
import com.chuca.reservationservice.domain.reservation.domain.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // 예약 신청
    @PostMapping("/reservations")
    public ResponseEntity<String> createReservation(@RequestBody ReservationRequestDTO request) {
        boolean isAvailable = reservationService.checkAndReserveSlot(request.getCafeId(), request.getReservationDate());

        if (!isAvailable) {
            return ResponseEntity.status(400).body("이미 예약된 시간대입니다.");
        }

        reservationService.createReservation(request);
        return ResponseEntity.status(201).body("예약이 신청되었습니다.");
    }

    // 예약 상태 조회
    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<ReservationResponseDTO> getReservationStatus(@PathVariable Long reservationId) {
        ReservationResponseDTO reservation = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    // 예약 취소
    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok("예약이 취소되었습니다.");
    }

    // 사장님용 예약 목록 조회 (승인 대기 중)
    @GetMapping("/admin/reservations")
    public ResponseEntity<List<ReservationResponseDTO>> getPendingReservations(@RequestParam String status) {
        List<ReservationResponseDTO> reservations = reservationService.getPendingReservations(status);
        return ResponseEntity.ok(reservations);
    }

    // 예약 승인
    @PutMapping("/admin/reservations/{reservationId}/approve")
    public ResponseEntity<String> approveReservation(@PathVariable Long reservationId) {
        reservationService.approveReservation(reservationId);
        return ResponseEntity.ok("예약이 승인되었습니다.");
    }

    // 예약 거절
    @PutMapping("/admin/reservations/{reservationId}/reject")
    public ResponseEntity<String> rejectReservation(@PathVariable Long reservationId) {
        reservationService.rejectReservation(reservationId);
        return ResponseEntity.ok("예약이 거절되었습니다.");
    }

    // 특정 시간대의 예약 가능 여부 확인
    @GetMapping("/cafes/{cafeId}/availability")
    public ResponseEntity<Boolean> checkCafeAvailability(@PathVariable Long cafeId) {
        boolean isAvailable = reservationService.checkCafeAvailability(cafeId);
        return ResponseEntity.ok(isAvailable);
    }
}}