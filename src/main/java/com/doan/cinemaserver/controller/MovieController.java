package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.movie.MovieRequestDto;
import com.doan.cinemaserver.domain.dto.movie.MovieSearchRequestDto;
import com.doan.cinemaserver.service.MovieService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestApiV1
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @Operation(summary = "API Create Movie")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = UrlConstant.Movie.CREATE_MOVIE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMovie( @ModelAttribute("requestDto") @Valid MovieRequestDto requestDto,
                                          @RequestParam(value = "image")  MultipartFile image) {
        return VsResponseUtil.success(movieService.createMovie(requestDto,image));
    }

    @Operation(summary = "API Update Movie")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = UrlConstant.Movie.UPDATE_MOVIE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMovie(@ModelAttribute("requestDto") @Valid MovieRequestDto requestDto,
                                         @RequestParam(value = "image",required = false)  MultipartFile image, @PathVariable Long id) {
        return VsResponseUtil.success(movieService.updateMovie(id,requestDto,image));
    }

    @Operation(summary = "API Search  Movie By Date")
    @PostMapping(value = UrlConstant.Movie.SEARCH_MOVIE_BY_DATE)
    public ResponseEntity<?> searchMovieByDate(@RequestBody MovieSearchRequestDto requestDto) {
        return VsResponseUtil.success(movieService.findMovieByDate(requestDto));
    }
    @Operation(summary = "API Search  Movie Coming Soon")
    @PostMapping(value = UrlConstant.Movie.SEARCH_MOVIE_COMING_SOON)
    public ResponseEntity<?> searchMovieComingSoon(@RequestBody MovieSearchRequestDto requestDto) {
        return VsResponseUtil.success(movieService.findMovieComingSoon(requestDto));
    }

    @Operation(summary = "API Get Movie Detail")
    @GetMapping(value = UrlConstant.Movie.GET_MOVIE_DETAIL)
    public ResponseEntity<?> getMovieDetail(@PathVariable Long id) {
        return VsResponseUtil.success(movieService.getMovieDetail(id));
    }
    @Operation(summary = "API Get All Movie")
    @PostMapping(value = UrlConstant.Movie.GET_ALL_MOVIE)
    public ResponseEntity<?> getAllMovies(@RequestBody  MovieSearchRequestDto requestDto) {
        return VsResponseUtil.success(movieService.getAllMovies(requestDto));
    }
    @Operation(summary = "API Delete Movie")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = UrlConstant.Movie.DELETE_MOVIE)
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        return VsResponseUtil.success(movieService.deleteMovie(id));
    }

    @Operation(summary = "API Get Movie Detail")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = UrlConstant.Movie.GET_MOVIE_SCHEDULE)
    public ResponseEntity<?> getMovieSchedule(@RequestParam  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")  LocalDate date) {
        return VsResponseUtil.success(movieService.getMovieSchedule(date));
    }

}
