package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.cinema.CinemaRequestDto;
import com.doan.cinemaserver.domain.dto.movietype.MovieTypeRequestDto;
import com.doan.cinemaserver.service.MovieTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiV1
@RequiredArgsConstructor
public class MovieTypeController {
    private final MovieTypeService movieTypeService;

    @Operation(summary = "API Create MovieType")
    @PostMapping(UrlConstant.MovieType.CREATE_TYPE)
    public ResponseEntity<?> createMovieType(@Valid @RequestBody MovieTypeRequestDto movieTypeRequestDto){
        return VsResponseUtil.success(movieTypeService.addMovieType(movieTypeRequestDto));
    }

    @Operation(summary = "API Update MovieType")
    @PatchMapping(UrlConstant.MovieType.UPDATE_TYPE)
    public ResponseEntity<?> updateCinema(@PathVariable(name = "id") Long typeId, @Valid @RequestBody MovieTypeRequestDto movieTypeRequestDto){
        return VsResponseUtil.success(movieTypeService.updateMovieType(typeId,movieTypeRequestDto));
    }

}
