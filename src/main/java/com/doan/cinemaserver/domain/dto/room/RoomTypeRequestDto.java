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
public class RoomTypeRequestDto {
    private Long id;
    private Long surcharge;
    private RoomTypeEnum roomType;
}
