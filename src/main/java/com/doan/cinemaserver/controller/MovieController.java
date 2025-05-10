package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.movie.MovieRequestDto;
import com.doan.cinemaserver.domain.dto.movie.MovieSearchRequestDto;
import com.doan.cinemaserver.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @Operation(summary = "API Create Movie")
    @PostMapping(value = UrlConstant.Movie.CREATE_MOVIE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMovie( @ModelAttribute("requestDto") @Valid MovieRequestDto requestDto,
                                          @RequestParam("image")  MultipartFile image) {
        return VsResponseUtil.success(movieService.createMovie(requestDto,image));
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
}
