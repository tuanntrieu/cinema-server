package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.cinema.CinemaRequestDto;
import com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.entity.Cinema;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.mapper.CinemaMapper;
import com.doan.cinemaserver.repository.CinemaRepository;
import com.doan.cinemaserver.service.CinemaService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository repository;
    private final CinemaMapper mapper;
    private final MessageSourceUtil messageSourceUtil;
    private final CinemaRepository cinemaRepository;

    @Override
    @Transactional
    public CommonResponseDto createCinema(CinemaRequestDto cinemaRequestDto) {

        cinemaRepository.save(mapper.toCinema(cinemaRequestDto));
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS,null));
    }

    @Override
    @Transactional
    public CommonResponseDto updateCinema(Long cinemaId, CinemaRequestDto cinemaRequestDto) {

        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(
                ()-> new NotFoundException(ErrorMessage.Cinema.ERR_NOT_FOUND_CINEMA,new String[]{cinemaId.toString()})
        );
        Cinema cinemaUpdate = mapper.toCinema(cinemaRequestDto);
        cinemaUpdate.setId(cinemaId);

        cinemaRepository.save(cinemaUpdate);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS,null));
    }

    @Override
    public List<String> loadProvince() {
        return cinemaRepository.loadProvince();
    }

    @Override
    public List<CinemaResponseDto> loadCinemasByProvince(String province) {
        return cinemaRepository.loadNameCinemaByProvince(province);
    }


}
