package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.cinema.CinemaRequestDto;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface CinemaService {
    public CommonResponseDto createCinema(CinemaRequestDto cinemaRequestDto);
    public CommonResponseDto updateCinema(Long cinemaId,CinemaRequestDto cinemaRequestDto);
}
