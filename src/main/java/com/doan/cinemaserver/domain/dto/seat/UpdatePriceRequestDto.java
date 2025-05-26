package com.doan.cinemaserver.domain.dto.seat;

import com.doan.cinemaserver.domain.dto.room.RoomTypeRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePriceRequestDto {
    private RoomTypeRequestDto[] roomRequest;
    private SeatTypeRequestDto[] seatRequest;
}
