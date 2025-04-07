package com.doan.cinemaserver.domain.dto.room;

import com.doan.cinemaserver.constant.CommonConstant;
import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.RoomTypeEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoomRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String name;

    private int numberOfRow = CommonConstant.ZERO_INT_VALUE;

    private int numberOfColumn = CommonConstant.ZERO_INT_VALUE;

    private RoomTypeEnum roomTypeEnum;

    private Long cinemaId;

}
