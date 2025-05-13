package com.doan.cinemaserver.domain.dto.ticket;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponseDto {
    private String id;
    private LocalDateTime createdDate;
    private String customerName;
    private String customerEmail;
    private String movieName;
    private String cinemaName;
    private String cinemaAddress;
    private String roomName;
    private LocalDate date;
    private LocalTime time;
    private String seats;
    private Long totalSeats;
    private Long totalCombos;
    private List<ComboTicketResponseDto> combo;
}
