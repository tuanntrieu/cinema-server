package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.seat.UpdatePriceRequestDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatStatusRequestDto;
import com.doan.cinemaserver.domain.entity.SeatPrice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeatService {

    public CommonResponseDto updateSeatStatus(UpdateSeatStatusRequestDto requestDto);

    public CommonResponseDto deleteSeat(Long seatId);

    public CommonResponseDto validateSeats(Long scheduleId, Long[] seatId);

    public CommonResponseDto holdingSeat(UpdateSeatStatusRequestDto requestDto, HttpServletRequest request);

    public CommonResponseDto unHoldSeat(UpdateSeatStatusRequestDto requestDto);

    public List<SeatPrice> getAllSeatPrices();

    public CommonResponseDto updatePrice(UpdatePriceRequestDto requestDto);
}
