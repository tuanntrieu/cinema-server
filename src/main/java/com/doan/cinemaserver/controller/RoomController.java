package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.RoomTypeEnum;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.room.RoomRequestDto;

import com.doan.cinemaserver.domain.dto.room.UpdateRoomSurchargeRequestDto;
import com.doan.cinemaserver.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @Operation(summary = "API Create Room")
    @PostMapping(UrlConstant.Room.CREATE_ROOM)
    public ResponseEntity<?> createSeat(@Valid @RequestBody RoomRequestDto requestDto) {
        return VsResponseUtil.success(roomService.createRoom(requestDto));
    }

    @Operation(summary = "API Update RoomType")
    @PatchMapping(UrlConstant.Room.UPDATE_ROOM_TYPE)
    public ResponseEntity<?> updateRoomType(@PathVariable(name = "id") Long id, @RequestParam RoomTypeEnum roomType) {
        return VsResponseUtil.success(roomService.updateRoomType(id, roomType));
    }

    @Operation(summary = "API Update Room Surcharge")
    @PatchMapping(UrlConstant.Room.UPDATE_ROOM_SURCHARGE)
    public ResponseEntity<?> updateRoomSurcharge(@RequestBody UpdateRoomSurchargeRequestDto requestDto) {
        return VsResponseUtil.success(roomService.updateRoomSurcharge(requestDto));
    }

    @Operation(summary = "API Update Delete Room")
    @DeleteMapping(UrlConstant.Room.DELETE_ROOM)
    public ResponseEntity<?> deleteRoom(@PathVariable(name = "id") Long id) {
        return VsResponseUtil.success(roomService.deleteRoom(id));
    }



}
