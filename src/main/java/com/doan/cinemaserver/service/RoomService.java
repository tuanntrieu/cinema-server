package com.doan.cinemaserver.service;

import com.doan.cinemaserver.constant.RoomTypeEnum;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomOrderResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomRequestDto;
import com.doan.cinemaserver.domain.dto.room.UpdateRoomSurchargeRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface RoomService {
    public CommonResponseDto createRoom(RoomRequestDto roomRequestDto);

    public CommonResponseDto updateRoomType(Long roomId, RoomTypeEnum roomTypeEnum);

    public CommonResponseDto updateRoomSurcharge(UpdateRoomSurchargeRequestDto requestDto);
    public CommonResponseDto deleteRoom(long roomId);

    public RoomOrderResponseDto getRoomOrder(Long scheduleId, HttpServletRequest request);
}
