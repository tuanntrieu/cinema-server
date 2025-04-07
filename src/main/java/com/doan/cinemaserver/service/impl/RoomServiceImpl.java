package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.constant.SeatType;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomRequestDto;
import com.doan.cinemaserver.domain.entity.*;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.mapper.RoomMapper;
import com.doan.cinemaserver.repository.*;
import com.doan.cinemaserver.service.RoomService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final CinemaRepository cinemaRepository;
    private final SeatRepository seatRepository;
    private final RoomMapper roomMapper;
    private final RoomTypeRepository roomTypeRepository;
    private final SeatPriceRepository seatPriceRepository;
    private final RoomRepository roomRepository;
    private final MessageSourceUtil messageSourceUtil;

    @Override
    public CommonResponseDto createRoom(RoomRequestDto roomRequestDto) {
        Cinema cinema = cinemaRepository.findById(roomRequestDto.getCinemaId()).orElseThrow(
                () -> new RuntimeException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM)
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
                        .seatName(columnLetter + String.valueOf(i + 1) + String.valueOf(j + 1))
                        .xCoordinate(i + 1)
                        .yCoordinate(j + 1)
                        .seatStatus(SeatStatus.AVAILABLE)
                        .surcharge(room.getRoomType().getSurcharge())
                        .seatType(seatPrice)
                        .build());
            }
        }
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS,null));
    }
}
