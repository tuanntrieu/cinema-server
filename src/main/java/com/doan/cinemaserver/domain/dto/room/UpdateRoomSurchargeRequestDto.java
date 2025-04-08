package com.doan.cinemaserver.domain.dto.room;

import com.doan.cinemaserver.constant.RoomTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoomSurchargeRequestDto {
    private RoomTypeEnum roomType;
    private Long surcharge;
}
