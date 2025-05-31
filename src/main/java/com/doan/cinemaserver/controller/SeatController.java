package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.seat.UpdatePriceRequestDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatStatusRequestDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatTypeRequestDto;
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

    @Operation(summary = "API Get ALl Seat Price")
    @GetMapping(UrlConstant.Seat.GET_ALL_SEAT_PRICE)
    public ResponseEntity<?> getAllSeatPrice() {
        return VsResponseUtil.success(seatService.getAllSeatPrices());
    }

    @Operation(summary = "API Update Price")
    @PatchMapping(UrlConstant.Seat.UPDATE_PRICE)
    public ResponseEntity<?> updatePrice(@RequestBody UpdatePriceRequestDto requestDto) {
        return VsResponseUtil.success(seatService.updatePrice(requestDto));
    }

    @Operation(summary = "API Maintain Seat")
    @PatchMapping(UrlConstant.Seat.MAINTAIN_SEATS)
    public ResponseEntity<?> maintainSeat(@RequestParam Long[] seatIds) {
        return VsResponseUtil.success(seatService.maintainSeats(seatIds));
    }
    @Operation(summary = "API Un-Maintain Seat")
    @PatchMapping(UrlConstant.Seat.UN_MAINTAIN_SEATS)
    public ResponseEntity<?> unMaintainSeat(@RequestParam Long[] seatIds) {
        return VsResponseUtil.success(seatService.unMaintainSeats(seatIds));
    }

    @Operation(summary = "API Update Vip Seat")
    @PatchMapping(UrlConstant.Seat.UPDATE_VIP_SEAT)
    public ResponseEntity<?> updateVipSeat(@RequestBody UpdateSeatTypeRequestDto requestDto) {
        return VsResponseUtil.success(seatService.updateVipSeats(requestDto));
    }
    @Operation(summary = "API Update Couple Seat")
    @PatchMapping(UrlConstant.Seat.UPDATE_COUPLE_SEAT)
    public ResponseEntity<?> updateCoupleSeat(@RequestBody UpdateSeatTypeRequestDto requestDto) {
        return VsResponseUtil.success(seatService.updateCoupleSeats(requestDto));
    }
    @Operation(summary = "API Update Standard Seat")
    @PatchMapping(UrlConstant.Seat.UPDATE_STANDARD_SEAT)
    public ResponseEntity<?> updateStandardSeat(@RequestBody UpdateSeatTypeRequestDto requestDto) {
        return VsResponseUtil.success(seatService.updateStandardSeats(requestDto));
    }

}
