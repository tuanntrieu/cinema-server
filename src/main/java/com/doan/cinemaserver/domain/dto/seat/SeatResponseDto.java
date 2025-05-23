package com.doan.cinemaserver.domain.dto.seat;

import com.doan.cinemaserver.constant.SeatStatus;
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
    private SeatStatus seatStatus;
    private int xCoordinate;
    private int yCoordinate;
    private Long price;
}
