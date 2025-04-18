package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.movietype.MovieTypeRequestDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.MovieType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieTypeService {
    public CommonResponseDto addMovieType(MovieTypeRequestDto movieTypeRequestDto);
    public CommonResponseDto updateMovieType(Long movieTypeId,MovieTypeRequestDto movieTypeRequestDto);
    public PaginationResponseDto<MovieType> getMovieTypePage(String name);
    public List<MovieType> getAllMovieType();
}
