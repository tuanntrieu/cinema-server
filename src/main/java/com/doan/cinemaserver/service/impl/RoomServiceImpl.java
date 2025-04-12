package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.*;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomRequestDto;
import com.doan.cinemaserver.domain.dto.room.UpdateRoomSurchargeRequestDto;
import com.doan.cinemaserver.domain.entity.*;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.mapper.RoomMapper;
import com.doan.cinemaserver.repository.*;
import com.doan.cinemaserver.service.RoomService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public CommonResponseDto createRoom(RoomRequestDto roomRequestDto) {
        Cinema cinema = cinemaRepository.findById(roomRequestDto.getCinemaId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM,new String[]{String.valueOf(roomRequestDto.getCinemaId())})
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
                        .surcharge(room.getRoomType().getSurcharge())
                        .seatType(seatPrice)
                        .build());
            }
        }
        cinema.getRooms().add(room);
        cinemaRepository.save(cinema);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS,null));
    }


    @Override
    @Transactional
    public CommonResponseDto updateRoomType(Long roomId, RoomTypeEnum roomTypeEnum) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                ()-> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM,new String[]{String.valueOf(roomId)})
        );
        RoomType roomType = roomTypeRepository.findByRoomType(roomTypeEnum).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM_TYPE, new String[]{roomTypeEnum.toString()})
        );
        room.setRoomType(roomType);
        roomRepository.save(room);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS,null));
    }

    @Override
    @Transactional
    public CommonResponseDto updateRoomSurcharge(UpdateRoomSurchargeRequestDto requestDto) {
        RoomType roomTypeTmp = roomTypeRepository.findByRoomType(requestDto.getRoomType()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM_TYPE, new String[]{requestDto.getRoomType().toString()})
        );
        roomTypeTmp.setSurcharge(requestDto.getSurcharge());
        roomTypeRepository.save(roomTypeTmp);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS,null));
    }

    @Override
    @Transactional
    public CommonResponseDto deleteRoom(long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                ()-> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM,new String[]{String.valueOf(roomId)})
        );
        room.getSeats().forEach(seat -> {
            seatRepository.delete(seat);
        });
        roomRepository.delete(room);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.DELETE_SUCCESS,null));
    }


}
