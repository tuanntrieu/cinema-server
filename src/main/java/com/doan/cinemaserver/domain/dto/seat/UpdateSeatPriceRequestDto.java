package com.doan.cinemaserver.domain.dto.seat;

import com.doan.cinemaserver.constant.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeatPriceRequestDto {
    private SeatType seatType;
    private Long weekDayPrice;
    private Long weekendPrice;
}
