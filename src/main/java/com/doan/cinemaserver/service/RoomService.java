package com.doan.cinemaserver.service;

import com.doan.cinemaserver.constant.RoomTypeEnum;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomOrderResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomRequestDto;
import com.doan.cinemaserver.domain.dto.room.UpdateRoomSurchargeRequestDto;
import com.doan.cinemaserver.domain.entity.RoomType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {
    public CommonResponseDto createRoom(RoomRequestDto roomRequestDto);

    public CommonResponseDto updateRoomType(Long roomId, RoomTypeEnum roomTypeEnum);


    public CommonResponseDto deleteRoom(long roomId);

    public RoomOrderResponseDto getRoomOrder(Long scheduleId, HttpServletRequest request);

    public List<RoomType> getALlRoomType();
}
