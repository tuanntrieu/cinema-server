package com.doan.cinemaserver.domain.dto.seat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponseDto {
    private Long seatId;
    private String seatName;
    private String seatType;
    private String seatStatus;
    private int xCoordinate;
    private int yCoordinate;
    private Long price;
}
