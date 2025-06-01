package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.movie.*;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public interface MovieService {
    public CommonResponseDto createMovie(MovieRequestDto movieRequestDto, MultipartFile image);
    public PaginationResponseDto<MovieResponseDto> findMovieByDate(MovieSearchRequestDto movieSearchRequestDto);
    public PaginationResponseDto<MovieResponseDto> findMovieComingSoon(MovieSearchRequestDto movieSearchRequestDto);
    public Movie getMovieDetail(Long movieId);
    public CommonResponseDto updateMovie(Long movieId, MovieRequestDto movieRequestDto,MultipartFile image);
    public PaginationResponseDto<MovieResponseDto> getAllMovies(MovieSearchRequestDto movieSearchRequestDto);
    public MovieDetailResponseDto getMovieDetail(long movieId);
    public CommonResponseDto deleteMovie(Long movieId);
    public List<MovieScheduleResponseDto> getMovieSchedule(LocalDate date);

}
