package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.room.RoomRequestDto;

import com.doan.cinemaserver.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiV1
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @Operation(summary = "API Create Seat")
    @PostMapping(UrlConstant.Room.CREATE_ROOM)
    public ResponseEntity<?> createSeat(@Valid @RequestBody RoomRequestDto requestDto){
        return VsResponseUtil.success(roomService.createRoom(requestDto));
    }

}
