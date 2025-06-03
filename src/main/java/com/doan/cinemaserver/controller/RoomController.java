package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.RoomTypeEnum;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.room.RoomRequestDto;
import com.doan.cinemaserver.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @Operation(summary = "API Create Room")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(UrlConstant.Room.CREATE_ROOM)
    public ResponseEntity<?> createSeat(@Valid @RequestBody RoomRequestDto requestDto) {
        return VsResponseUtil.success(roomService.createRoom(requestDto));
    }

    @Operation(summary = "API Update RoomType")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(UrlConstant.Room.UPDATE_ROOM_TYPE)
    public ResponseEntity<?> updateRoomType(@PathVariable(name = "id") Long id, @RequestParam RoomTypeEnum roomType) {
        return VsResponseUtil.success(roomService.updateRoomType(id, roomType));
    }

    @Operation(summary = "API Delete Room")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(UrlConstant.Room.DELETE_ROOM)
    public ResponseEntity<?> deleteRoom(@PathVariable(name = "id") Long id) {
        return VsResponseUtil.success(roomService.deleteRoom(id));
    }

    @Operation(summary = "API Get Room Order")
    @GetMapping(UrlConstant.Room.GET_ROOM_ORDER)
    public ResponseEntity<?> getRoomOrder(@PathVariable(name = "id") Long scheduleId, HttpServletRequest request) {
        return VsResponseUtil.success(roomService.getRoomOrder(scheduleId, request));
    }

    @Operation(summary = "API Get All RoomType")
    @GetMapping(UrlConstant.Room.GET_ALL_ROOM_TYPES)
    public ResponseEntity<?> getALlRoomTye() {
        return VsResponseUtil.success(roomService.getALlRoomType());
    }


    @Operation(summary = "API Validate Room")
    @GetMapping(UrlConstant.Room.VALIDATE_ROOM)
    public ResponseEntity<?> validateRoom(@PathVariable(name = "id") Long id) {
        return VsResponseUtil.success(roomService.validateRoom(id));
    }

    @Operation(summary = "API Get Room Detail")
    @GetMapping(UrlConstant.Room.GET_ROOM_DETAIL)
    public ResponseEntity<?> getRoomDetail(@PathVariable(name = "id") Long id) {
        return VsResponseUtil.success(roomService.getRoomDetail(id));
    }

}
