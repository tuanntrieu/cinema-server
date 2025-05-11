package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.*;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomOrderResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomRequestDto;
import com.doan.cinemaserver.domain.dto.room.UpdateRoomSurchargeRequestDto;
import com.doan.cinemaserver.domain.dto.seat.SeatResponseDto;
import com.doan.cinemaserver.domain.entity.*;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.exception.UnauthorizedException;
import com.doan.cinemaserver.mapper.RoomMapper;
import com.doan.cinemaserver.repository.*;
import com.doan.cinemaserver.security.jwt.JwtTokenProvider;
import com.doan.cinemaserver.service.RoomService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final CinemaRepository cinemaRepository;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final RoomMapper roomMapper;
    private final RoomTypeRepository roomTypeRepository;
    private final SeatPriceRepository seatPriceRepository;
    private final RoomRepository roomRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    @Transactional
    public CommonResponseDto createRoom(RoomRequestDto roomRequestDto) {
        Cinema cinema = cinemaRepository.findById(roomRequestDto.getCinemaId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM, new String[]{String.valueOf(roomRequestDto.getCinemaId())})
        );
        Room room = roomMapper.toRoom(roomRequestDto);
        room.setCinema(cinema);
        RoomType roomType = roomTypeRepository.findByRoomType(roomRequestDto.getRoomTypeEnum()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM_TYPE, new String[]{roomRequestDto.getRoomTypeEnum().toString()})
        );
        room.setRoomType(roomType);
        room.setRoomType(roomType);
        roomRepository.save(room);
        SeatPrice seatPrice = seatPriceRepository.findBySeatType(SeatType.STANDARD).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Seat.ERR_NOT_FOUND_SEAT_TYPE, new String[]{SeatType.STANDARD.toString()})
        );
        for (int i = 0; i < roomRequestDto.getNumberOfColumn(); i++) {
            char columnLetter = (char) ('A' + i);
            for (int j = 0; j < roomRequestDto.getNumberOfRow(); j++) {
                seatRepository.save(Seat.builder()
                        .room(room)
                        .seatName(columnLetter + String.valueOf(j + 1))
                        .xCoordinate(i + 1)
                        .yCoordinate(j + 1)
                        .seatType(seatPrice)
                        .build());
            }
        }
        cinema.getRooms().add(room);
        cinemaRepository.save(cinema);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }


    @Override
    @Transactional
    public CommonResponseDto updateRoomType(Long roomId, RoomTypeEnum roomTypeEnum) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM, new String[]{String.valueOf(roomId)})
        );
        RoomType roomType = roomTypeRepository.findByRoomType(roomTypeEnum).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM_TYPE, new String[]{roomTypeEnum.toString()})
        );
        room.setRoomType(roomType);
        roomRepository.save(room);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
    }

    @Override
    @Transactional
    public CommonResponseDto updateRoomSurcharge(UpdateRoomSurchargeRequestDto requestDto) {
        RoomType roomTypeTmp = roomTypeRepository.findByRoomType(requestDto.getRoomType()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM_TYPE, new String[]{requestDto.getRoomType().toString()})
        );
        roomTypeTmp.setSurcharge(requestDto.getSurcharge());
        roomTypeRepository.save(roomTypeTmp);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
    }

    @Override
    @Transactional
    public CommonResponseDto deleteRoom(long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM, new String[]{String.valueOf(roomId)})
        );
        seatRepository.deleteAll(room.getSeats());
        roomRepository.delete(room);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.DELETE_SUCCESS, null));
    }

    @Override
    public RoomOrderResponseDto getRoomOrder(Long scheduleId, HttpServletRequest request) {
        String token = getJwtFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }
        String email = jwtTokenProvider.extractSubjectFromJwt(token);

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Schedule.ERR_NOT_FOUND_SCHEDULE, new String[]{String.valueOf(scheduleId)})
        );
        Movie movie = schedule.getMovie();
        Room room = schedule.getRoom();
        Cinema cinema = room.getCinema();
        Long surcharge = room.getRoomType().getSurcharge();

        StringBuilder typeBuilder = new StringBuilder();
        movie.getTypes().forEach(t -> {
            typeBuilder.append(t.getName()).append(", ");
        });
        String types = !typeBuilder.isEmpty()
                ? typeBuilder.substring(0, typeBuilder.length() - 2)
                : "";
        RoomOrderResponseDto response = new RoomOrderResponseDto();
        response.setCinemaId(cinema.getId());
        response.setCinemaName(cinema.getCinemaName());
        response.setMovieId(movie.getId());
        response.setMovieName(movie.getName());
        response.setLanguage(movie.getLanguage() + (movie.getIsSub() ? "(Phụ đề)" : ""));
        response.setDuration(movie.getDuration());
        response.setMovieImageUrl(movie.getImage());
        response.setMovieType(types);
        response.setDate(schedule.getScheduleTime().toLocalDate());
        response.setTime(schedule.getScheduleTime().toLocalTime());
        response.setRoomId(room.getId());
        response.setAgeLimit(movie.getAgeLimit());
        response.setRoomName(room.getName());
        response.setRoomType(room.getRoomType().getRoomType().getValue());
        List<ScheduleSeat> scheduleSeats = schedule.getScheduleSeats();
        List<SeatResponseDto> seatsResponse = new ArrayList<>();
        SeatStatus seatStatus;
        for (ScheduleSeat scheduleSeat : scheduleSeats) {
            Seat seat = seatRepository.findById(scheduleSeat.getSeat().getId()).orElse(null);

            if (seat != null) {
                DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
                Long price = 0L;
                if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                    price = seat.getSeatType().getWeekendPrice();
                } else {
                    price = seat.getSeatType().getWeekdayPrice();
                }
                if (scheduleSeat.getEmail() != null && scheduleSeat.getSeatStatus().equals(SeatStatus.HOLDING)) {
                    if ( scheduleSeat.getEmail().equals(email)) {
                        seatStatus = SeatStatus.SELECTED;
                    }else {
                        seatStatus = SeatStatus.HOLDING;
                    }
                }
                else {
                    seatStatus = scheduleSeat.getSeatStatus();
                }
                seatsResponse.add(SeatResponseDto.builder()
                        .seatId(scheduleSeat.getSeat().getId())
                        .seatStatus(seatStatus)
                        .xCoordinate(seat.getXCoordinate())
                        .yCoordinate(seat.getYCoordinate())
                        .price(price + surcharge)
                        .seatName(seat.getSeatName())
                        .seatType(seat.getSeatType().getSeatType().toString())
                        .build());

            }
        }
        response.setSeats(seatsResponse);
        return response;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
