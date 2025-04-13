package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.room.UpdateRoomSurchargeRequestDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatPriceRequestDto;
import com.doan.cinemaserver.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @Operation(summary = "API Update Seat Type")
    @PatchMapping(UrlConstant.Seat.UPDATE_SEAT)
    public ResponseEntity<?> updateSeatPrice(@RequestBody UpdateSeatPriceRequestDto requestDto) {
        return VsResponseUtil.success(seatService.updateSeatPrice(requestDto));
    }

    @Operation(summary = "API Update Delete Seat")
    @DeleteMapping(UrlConstant.Seat.DELETE_SEAT)
    public ResponseEntity<?> deleteSeat(@PathVariable(name = "id") Long id) {
        return VsResponseUtil.success(seatService.deleteSeat(id));
    }

    @Operation(summary = "API Validate Seat")
    @GetMapping(UrlConstant.Seat.VALIDATE_SEATS)
    public ResponseEntity<?> validateSeats(@PathVariable Long id,@RequestParam Long [] seatId) {
        return VsResponseUtil.success(seatService.validateSeats(id,seatId));
    }

}
