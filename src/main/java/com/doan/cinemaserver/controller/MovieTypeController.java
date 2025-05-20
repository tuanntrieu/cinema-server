package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.movietype.MovieTypeRequestDto;
import com.doan.cinemaserver.domain.dto.movietype.MovieTypeSearchRequestDto;
import com.doan.cinemaserver.service.MovieTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class  MovieTypeController {
    private final MovieTypeService movieTypeService;

    @Operation(summary = "API Create MovieType")
    @PostMapping(UrlConstant.MovieType.CREATE_TYPE)
    public ResponseEntity<?> createMovieType(@Valid @RequestBody MovieTypeRequestDto movieTypeRequestDto){
        return VsResponseUtil.success(movieTypeService.addMovieType(movieTypeRequestDto));
    }

    @Operation(summary = "API Update MovieType")
    @PatchMapping(UrlConstant.MovieType.UPDATE_TYPE)
    public ResponseEntity<?> updateMovieType(@PathVariable Long id, @Valid @RequestBody MovieTypeRequestDto movieTypeRequestDto){
        return VsResponseUtil.success(movieTypeService.updateMovieType(id,movieTypeRequestDto));
    }
    @Operation(summary = "API Delete MovieType")
    @DeleteMapping(UrlConstant.MovieType.DELETE_TYPE)
    public ResponseEntity<?> deleteMovieType(@PathVariable Long id){
        return VsResponseUtil.success(movieTypeService.deleteMovieType(id));
    }


    @Operation(summary = "API Get All MovieType")
    @GetMapping(UrlConstant.MovieType.GET_MOVIE_TYPE)
    public ResponseEntity<?> getAllMovieType(){
        return VsResponseUtil.success(movieTypeService.getAllMovieType());
    }

    @Operation(summary = "API Get All MovieType Page")
    @PostMapping(UrlConstant.MovieType.GET_MOVIE_TYPE_PAGE)
    public ResponseEntity<?> getAllMovieTypePage(@RequestBody MovieTypeSearchRequestDto requestDto){
        return VsResponseUtil.success(movieTypeService.getMovieTypePage(requestDto));
    }

}
