package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.constant.SeatType;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatPriceRequestDto;
import com.doan.cinemaserver.domain.entity.Room;
import com.doan.cinemaserver.domain.entity.Schedule;
import com.doan.cinemaserver.domain.entity.Seat;
import com.doan.cinemaserver.domain.entity.SeatPrice;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.ScheduleRepository;
import com.doan.cinemaserver.repository.SeatPriceRepository;
import com.doan.cinemaserver.repository.SeatRepository;
import com.doan.cinemaserver.service.SeatService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SeatServiceImpl implements SeatService {
    private final SeatPriceRepository seatPriceRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public CommonResponseDto updateSeatPrice(UpdateSeatPriceRequestDto requestDto) {
        SeatPrice seatPrice = seatPriceRepository.findBySeatType(requestDto.getSeatType()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Seat.ERR_NOT_FOUND_SEAT_TYPE, new String[]{requestDto.getSeatType().toString()})
        );
        seatPriceRepository.updateSeatPrice(requestDto.getSeatType().toString(), requestDto.getWeekDayPrice(), requestDto.getWeekendPrice());

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
    @Override
    public CommonResponseDto validateSeats(Long scheduleId, Long[] seatId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Schedule.ERR_NOT_FOUND_SCHEDULE, new String[]{scheduleId.toString()})
        );
        Room room = schedule.getRoom();
        List<Seat> allSeats = room.getSeats();
        List<Seat> selectedSeats = new ArrayList<>();
        Map<Long, SeatStatus> seatStatus = schedule.getSeats();
        Map<Integer, Map<Integer, Seat>> seatMap = new HashMap<>();
        for (Seat seat : allSeats) {
            seatMap
                    .computeIfAbsent(seat.getXCoordinate(), k -> new HashMap<>())
                    .put(seat.getYCoordinate(), seat);
        }
        for (int i = 0; i < seatId.length; i++) {
            int finalI = i;
            Seat seat = seatRepository.findById(seatId[i]).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.Seat.ERR_NOT_FOUND_SEAT, new String[]{String.valueOf(seatId[finalI])})
            );
            selectedSeats.add(seat);
        }

        for (Seat seat : selectedSeats) {
            if (!SeatType.COUPLE.equals(seat.getSeatType())) {
                int x = seat.getXCoordinate();
                int y = seat.getYCoordinate();

                Map<Integer, Seat> row = seatMap.get(x);
                int minY = Collections.min(row.keySet());
                int maxY = Collections.max(row.keySet());

                // Kiem tra ghe dau tien co trong khong
                if (y == minY + 1) {
                    Seat left = row.get(y - 1);
                    if (left != null && !selectedSeats.contains(left)
                            && SeatStatus.AVAILABLE.equals(seatStatus.get(left.getId()))) {
                        return new CommonResponseDto(messageSourceUtil.getMessage(ErrorMessage.Seat.ERR_NOT_EMPTY_SEAT_OUTSIDE, null), false);

                    }
                }
                // Kiem tra ghe cuoi co trong khong
                if (y == maxY - 1) {
                    Seat right = row.get(y + 1);
                    if (right != null && !selectedSeats.contains(right)
                            && SeatStatus.AVAILABLE.equals(seatStatus.get(right.getId()))) {
                        return new CommonResponseDto(messageSourceUtil.getMessage(ErrorMessage.Seat.ERR_NOT_EMPTY_SEAT_OUTSIDE, null), false);

                    }
                }
                // Kiem tra ghe o giua co trong khong
                Seat mid = row.get(y + 1);
                Seat far = row.get(y + 2);
                if (far != null && selectedSeats.contains(far)
                        && mid != null && !selectedSeats.contains(mid)
                        && SeatStatus.AVAILABLE.equals(seatStatus.get(mid.getId()))) {
                    return new CommonResponseDto(messageSourceUtil.getMessage(ErrorMessage.Seat.ERR_NOT_EMPTY_SEAT_MIDDLE, null), false);

                }
            }
        }
        return new CommonResponseDto("Seats is valid", true);
    }
}
