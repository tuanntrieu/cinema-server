package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.cinema.CinemaRequestDto;
import com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CinemaService {
    public CommonResponseDto createCinema(CinemaRequestDto cinemaRequestDto);
    public CommonResponseDto updateCinema(Long cinemaId,CinemaRequestDto cinemaRequestDto);
    public List<String> loadProvince();
    public List<CinemaResponseDto>loadCinemasByProvince(String province);
}
