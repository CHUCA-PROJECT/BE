package com.chuca.reservationservice.domain.reservation.application.dto;
import lombok.Data;
import java.time.LocalDate;
@Data

public class ReservationResponseDTO {

    private Long reservationId;
    private LocalDate reservationDate;
    private LocalDate periodDate;
    private LocalDate prePeriodDate;
    private LocalDate demolishDate;
    private LocalDate businessHours;
    private int reservationStatus;
    private String birthDayName;
    private String genre;
    private String eventName;
    private Integer headCount;
    private String imgUrl;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long memberId;
    private Long cafeId;
}
