package com.doan.cinemaserver.domain.dto.room;

import com.doan.cinemaserver.constant.RoomTypeEnum;
import com.doan.cinemaserver.domain.dto.seat.SeatResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDetailResponseDto {
    private Long id;
    private String name;
    private RoomTypeEnum roomType;
    private List<SeatResponseDto> seats =new ArrayList<>();
}
