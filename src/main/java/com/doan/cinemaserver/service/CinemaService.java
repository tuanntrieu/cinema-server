package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.cinema.*;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationRequestDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomResponseDto;
import com.doan.cinemaserver.domain.entity.Cinema;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CinemaService {
    public CommonResponseDto createCinema(CinemaRequestDto cinemaRequestDto);
    public CommonResponseDto updateCinema(Long cinemaId,CinemaRequestDto cinemaRequestDto);
    public List<String> loadProvince();
    public List<CinemaResponseDto>loadCinemasByProvince(String province);
    public List<CinemaResponseDto> loadAllCinemas();
    public PaginationResponseDto<CinemaResponsePageDto>getAllCinema(CinemaSearchRequestDto requestDto);
    public Cinema getCinemaDetail(Long id);
    public PaginationResponseDto<RoomResponseDto>getRoomByCinema(CinemaGetRoomRequestDto paginationRequestDto);
 }
