package com.doan.cinemaserver.mapper;

import com.doan.cinemaserver.domain.dto.room.RoomRequestDto;
import com.doan.cinemaserver.domain.entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toRoom(RoomRequestDto roomRequestDto);
}

