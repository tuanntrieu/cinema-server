package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface RoomService {
    public CommonResponseDto createRoom(RoomRequestDto roomRequestDto);
}
