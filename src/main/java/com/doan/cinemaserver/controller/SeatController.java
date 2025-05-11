package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatPriceRequestDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatStatusRequestDto;
import com.doan.cinemaserver.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @Operation(summary = "API Update Seat Price")
    @PatchMapping(UrlConstant.Seat.UPDATE_SEAT_PRICE)
    public ResponseEntity<?> updateSeatPrice(@RequestBody UpdateSeatPriceRequestDto requestDto) {
        return VsResponseUtil.success(seatService.updateSeatPrice(requestDto));
    }

    @Operation(summary = "API Update Seat Status")
    @PatchMapping(UrlConstant.Seat.UPDATE_SEAT_STATUS)
    public ResponseEntity<?> updateSeatStatus(@RequestBody UpdateSeatStatusRequestDto requestDto) {
        return VsResponseUtil.success(seatService.updateSeatStatus(requestDto));
    }

    @Operation(summary = "API Hold Seat")
    @PatchMapping(UrlConstant.Seat.HOLD_SEAT)
    public ResponseEntity<?> holdSeat(@RequestBody UpdateSeatStatusRequestDto requestDto, HttpServletRequest request) {
        return VsResponseUtil.success(seatService.holdingSeat(requestDto, request));
    }

    @Operation(summary = "API UnHold Seat")
    @PatchMapping(UrlConstant.Seat.UNHOLD_SEAT)
    public ResponseEntity<?> unHoldSeat(@RequestBody UpdateSeatStatusRequestDto requestDto) {
        return VsResponseUtil.success(seatService.unHoldSeat(requestDto));
    }

    @Operation(summary = "API  Delete Seat")
    @DeleteMapping(UrlConstant.Seat.DELETE_SEAT)
    public ResponseEntity<?> deleteSeat(@PathVariable(name = "id") Long id) {
        return VsResponseUtil.success(seatService.deleteSeat(id));
    }

    @Operation(summary = "API Validate Seat")
    @GetMapping(UrlConstant.Seat.VALIDATE_SEATS)
    public ResponseEntity<?> validateSeats(@PathVariable Long id, @RequestParam Long[] seatId) {
        return VsResponseUtil.success(seatService.validateSeats(id, seatId));
    }

}
