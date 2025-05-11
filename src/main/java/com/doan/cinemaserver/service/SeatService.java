package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatPriceRequestDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatStatusRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface SeatService {
    public CommonResponseDto updateSeatPrice(UpdateSeatPriceRequestDto requestDto);

    public CommonResponseDto updateSeatStatus(UpdateSeatStatusRequestDto requestDto);

    public CommonResponseDto deleteSeat(Long seatId);

    public CommonResponseDto validateSeats(Long scheduleId, Long[] seatId);

    public CommonResponseDto holdingSeat(UpdateSeatStatusRequestDto requestDto, HttpServletRequest request);

    public CommonResponseDto unHoldSeat(UpdateSeatStatusRequestDto requestDto);
}
