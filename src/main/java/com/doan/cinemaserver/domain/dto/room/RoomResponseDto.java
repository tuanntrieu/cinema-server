package com.doan.cinemaserver.domain.dto.room;

import com.doan.cinemaserver.constant.RoomTypeEnum;
import com.doan.cinemaserver.domain.entity.RoomType;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponseDto {
    private Long id;
    private String name;
    private RoomTypeEnum roomType;
    private Long sumSeat;
}
