package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SeatType;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.entity.Seat;
import com.doan.cinemaserver.domain.entity.SeatPrice;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.SeatPriceRepository;
import com.doan.cinemaserver.repository.SeatRepository;
import com.doan.cinemaserver.service.SeatService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SeatServiceImpl implements SeatService {
    private final SeatPriceRepository seatPriceRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final SeatRepository seatRepository;

    @Override
    public CommonResponseDto updateSeatPrice(SeatType seatType, Long weekDayPrice,Long weekendPrice) {
        SeatPrice seatPrice = seatPriceRepository.findBySeatType(seatType).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Seat.ERR_NOT_FOUND_SEAT_TYPE, new String[]{seatType.toString()})
        );
        seatPriceRepository.updateSeatPrice(seatType,weekDayPrice,weekendPrice);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS,null));
    }

    @Override
    @Transactional
    public CommonResponseDto deleteSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(
                ()-> new NotFoundException(ErrorMessage.Seat.ERR_NOT_FOUND_SEAT, new String[]{seatId.toString()})
        );
        seatRepository.delete(seat);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.DELETE_SUCCESS,null));

    }
}
