package com.chuca.reservationservice.domain.reservation.domain.repository;

import com.chuca.reservationservice.domain.reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByReservationStatus(int status);
}
