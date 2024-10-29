package com.chuca.reservationservice.domain.reservation.application.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservationRequestDTO {

    private LocalDate reservationDate;
    private LocalDate periodDate;
    private LocalDate prePeriodDate;
    private LocalDate demolishDate;
    private LocalDate businessHours;
    private String birthDayName;
    private String genre;
    private String eventName;
    private Integer headCount;
    private String imgUrl;
    private Long memberId;
    private Long cafeId;
}

