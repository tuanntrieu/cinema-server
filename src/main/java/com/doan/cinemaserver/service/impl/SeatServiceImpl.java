package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.constant.SeatType;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.seat.UpdatePriceRequestDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatStatusRequestDto;
import com.doan.cinemaserver.domain.entity.*;
import com.doan.cinemaserver.exception.InvalidException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.exception.UnauthorizedException;
import com.doan.cinemaserver.repository.*;
import com.doan.cinemaserver.security.jwt.JwtTokenProvider;
import com.doan.cinemaserver.service.SeatService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SeatServiceImpl implements SeatService {
    private final SeatPriceRepository seatPriceRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ScheduleSeatRepository scheduleSeatRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final RoomTypeRepository roomTypeRepository;



    @Override
    public CommonResponseDto updateSeatStatus(UpdateSeatStatusRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Schedule.ERR_NOT_FOUND_SCHEDULE, new String[]{String.valueOf(requestDto.getScheduleId())})
        );
        ScheduleSeat scheduleSeat = schedule.getScheduleSeats().stream().filter(
                s -> s.getSeat().getId() == requestDto.getSeatId()).findFirst().orElseThrow(
                () -> new InvalidException(ErrorMessage.ERR_EXCEPTION_GENERAL)
        );
        scheduleSeat.setSeatStatus(requestDto.getSeatStatus());
        scheduleSeatRepository.save(scheduleSeat);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
    }

    @Override
    @Transactional
    public CommonResponseDto deleteSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Seat.ERR_NOT_FOUND_SEAT, new String[]{seatId.toString()})
        );
        seatRepository.delete(seat);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.DELETE_SUCCESS, null));

    }

    @Override
    public CommonResponseDto validateSeats(Long scheduleId, Long[] seatId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Schedule.ERR_NOT_FOUND_SCHEDULE, new String[]{scheduleId.toString()})
        );
        Room room = schedule.getRoom();
        List<Seat> allSeats = room.getSeats();
        List<Seat> selectedSeats = new ArrayList<>();
        Map<Long, ScheduleSeat> scheduleSeatMap = schedule.getScheduleSeats()
                .stream()
                //        .filter(ss -> !ss.getSeatStatus().equals(SeatStatus.MAINTENANCE))
                .collect(Collectors.toMap(ss -> ss.getSeat().getId(), ss -> ss));


        Map<Integer, Map<Integer, Seat>> seatMap = new HashMap<>();
        for (Seat seat : allSeats) {
            if (!scheduleSeatMap.get(seat.getId()).getSeatStatus().equals(SeatStatus.MAINTENANCE)) {
                seatMap
                        .computeIfAbsent(seat.getXCoordinate(), k -> new HashMap<>())
                        .put(seat.getYCoordinate(), seat);
            }
        }

        //lay danh sach ghe dc chon
        for (int i = 0; i < seatId.length; i++) {
            int finalI = i;
            Seat seatSelected = seatRepository.findById(seatId[i]).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.Seat.ERR_NOT_FOUND_SEAT, new String[]{String.valueOf(seatId[finalI])})
            );

            ScheduleSeat ss = scheduleSeatMap.get(seatSelected.getId());
            if (ss == null || !SeatStatus.HOLDING.equals(ss.getSeatStatus())) {
                throw new InvalidException(ErrorMessage.Seat.ERR_INVALID_SEAT_STATUS);
            }
            selectedSeats.add(seatSelected);
        }

        for (Seat seat : selectedSeats) {
            if (!SeatType.COUPLE.equals(seat.getSeatType().getSeatType())) {
                int x = seat.getXCoordinate();
                int y = seat.getYCoordinate();

                Map<Integer, Seat> row = seatMap.get(x);
                int minY = Collections.min(row.keySet());
                int maxY = Collections.max(row.keySet());

                // Ghế đầu hàng
                if (y == minY + 1) {
                    Seat left = row.get(y - 1);
                    if (left != null && !selectedSeats.contains(left)) {
                        ScheduleSeat leftSS = scheduleSeatMap.get(left.getId());
                        if (leftSS != null && SeatStatus.AVAILABLE.equals(leftSS.getSeatStatus())) {
                            return new CommonResponseDto(messageSourceUtil.getMessage(ErrorMessage.Seat.ERR_NOT_EMPTY_SEAT_OUTSIDE, null), false);
                        }
                    }
                }

                // Ghế cuối hàng
                if (y == maxY - 1) {
                    Seat right = row.get(y + 1);
                    if (right != null && !selectedSeats.contains(right)) {
                        ScheduleSeat rightSS = scheduleSeatMap.get(right.getId());
                        if (rightSS != null && SeatStatus.AVAILABLE.equals(rightSS.getSeatStatus())) {
                            return new CommonResponseDto(messageSourceUtil.getMessage(ErrorMessage.Seat.ERR_NOT_EMPTY_SEAT_OUTSIDE, null), false);
                        }
                    }
                }

                // Ghế ở giữa bị trống
                Seat mid = row.get(y + 1);
                Seat far = row.get(y + 2);
                if (far != null && selectedSeats.contains(far)
                        && mid != null && !selectedSeats.contains(mid)) {
                    ScheduleSeat midSS = scheduleSeatMap.get(mid.getId());
                    if (midSS != null && SeatStatus.AVAILABLE.equals(midSS.getSeatStatus())) {
                        return new CommonResponseDto(messageSourceUtil.getMessage(ErrorMessage.Seat.ERR_NOT_EMPTY_SEAT_MIDDLE, null), false);
                    }
                }
            }
        }

        return new CommonResponseDto("Hợp lệ", true);
    }

    @Override
    public CommonResponseDto holdingSeat(UpdateSeatStatusRequestDto requestDto, HttpServletRequest request) {
        String token = getJwtFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }
        String email = jwtTokenProvider.extractSubjectFromJwt(token);
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Schedule.ERR_NOT_FOUND_SCHEDULE, new String[]{String.valueOf(requestDto.getScheduleId())})
        );
        ScheduleSeat scheduleSeat = schedule.getScheduleSeats().stream().filter(
                s -> s.getSeat().getId() == requestDto.getSeatId()).findFirst().orElseThrow(
                () -> new InvalidException(ErrorMessage.ERR_EXCEPTION_GENERAL)
        );
        if (scheduleSeat.getSeatStatus().equals(SeatStatus.AVAILABLE)) {
            scheduleSeat.setSeatStatus(SeatStatus.HOLDING);
            scheduleSeat.setEmail(email);
            scheduleSeatRepository.save(scheduleSeat);
            String redisKey = "schedule:" + requestDto.getScheduleId() + "-seat:" + requestDto.getSeatId();
            String redisValue = "1";
            redisTemplate.opsForValue().set(redisKey, redisValue, Duration.ofMinutes(10));
            messagingTemplate.convertAndSend("/topic/seat-expired/" + schedule.getId(), requestDto.getSeatId());
        } else {
            throw new InvalidException(ErrorMessage.Seat.ERR_INVALID_SEAT_STATUS);
        }
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
    }

    @Override
    public CommonResponseDto unHoldSeat(UpdateSeatStatusRequestDto requestDto) {

        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Schedule.ERR_NOT_FOUND_SCHEDULE, new String[]{String.valueOf(requestDto.getScheduleId())})
        );
        ScheduleSeat scheduleSeat = schedule.getScheduleSeats().stream().filter(
                s -> s.getSeat().getId() == requestDto.getSeatId()).findFirst().orElseThrow(
                () -> new InvalidException(ErrorMessage.ERR_EXCEPTION_GENERAL)
        );

        if (scheduleSeat.getSeatStatus().equals(SeatStatus.HOLDING)) {
            String redisKey = "schedule:" + requestDto.getScheduleId() + "-seat:" + requestDto.getSeatId();
            redisTemplate.delete(redisKey);
            scheduleSeat.setSeatStatus(SeatStatus.AVAILABLE);
            scheduleSeat.setEmail(null);
            scheduleSeatRepository.save(scheduleSeat);
            messagingTemplate.convertAndSend("/topic/seat-expired/" + schedule.getId(), requestDto.getSeatId());
        } else {
            throw new InvalidException(ErrorMessage.Seat.ERR_INVALID_SEAT_STATUS);
        }
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));

    }

    @Override
    public List<SeatPrice> getAllSeatPrices() {
        return seatPriceRepository.findAll();
    }

    @Override
    @Transactional
    public CommonResponseDto updatePrice(UpdatePriceRequestDto requestDto) {
        Arrays.stream(requestDto.getRoomRequest()).forEach(
                room -> {
                    roomTypeRepository.updateRoomSurcharge(room.getRoomType().toString(),room.getSurcharge());
                }
        );
        Arrays.stream(requestDto.getSeatRequest()).forEach(
                seat -> {
                    seatPriceRepository.updateSeatPrice(seat.getSeatType().toString(),seat.getWeekdayPrice(), seat.getWeekendPrice());
                }
        );
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}
