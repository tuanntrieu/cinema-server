package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.movietype.MovieTypeRequestDto;
import com.doan.cinemaserver.domain.entity.MovieType;
import org.springframework.stereotype.Service;

@Service
public interface MovieTypeService {
    public CommonResponseDto addMovieType(MovieTypeRequestDto movieTypeRequestDto);
    public CommonResponseDto updateMovieType(Long movieTypeId,MovieTypeRequestDto movieTypeRequestDto);
}
