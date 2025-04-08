package com.doan.cinemaserver.service;

import com.doan.cinemaserver.constant.SeatType;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface SeatService {
    public CommonResponseDto updateSeatPrice(SeatType seatType, Long weekDayPrice, Long weekendPrice);
    public CommonResponseDto deleteSeat(Long seatId);
}
