package com.doan.cinemaserver.domain.dto.seat;

import com.doan.cinemaserver.constant.SeatStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSeatStatusRequestDto {
    private long scheduleId;
    private long seatId;
    private SeatStatus seatStatus;
}
