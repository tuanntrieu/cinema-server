package com.doan.cinemaserver.service;

import com.doan.cinemaserver.constant.SeatType;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatPriceRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface SeatService {
    public CommonResponseDto updateSeatPrice(UpdateSeatPriceRequestDto requestDto);
    public CommonResponseDto deleteSeat(Long seatId);
}
