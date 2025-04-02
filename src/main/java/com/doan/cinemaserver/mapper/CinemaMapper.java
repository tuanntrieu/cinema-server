package com.doan.cinemaserver.mapper;

import com.doan.cinemaserver.domain.dto.cinema.CinemaRequestDto;
import com.doan.cinemaserver.domain.entity.Cinema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CinemaMapper {

    public Cinema toCinema(CinemaRequestDto cinemaRequestDto);
}
