package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.cinema.CinemaRequestDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerDto;
import com.doan.cinemaserver.service.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @Operation(summary = "API Create Cinema")
    @PostMapping(UrlConstant.Cinema.CREATE_CINEMA)
    public ResponseEntity<?> createCinema(@Valid @RequestBody CinemaRequestDto cinemaRequestDto){
        return VsResponseUtil.success(cinemaService.createCinema(cinemaRequestDto));
    }

    @Operation(summary = "API Update Cinema")
    @PatchMapping(UrlConstant.Cinema.UPDATE_CINEMA)
    public ResponseEntity<?> updateCinema(@PathVariable(name = "id") Long cinemaId, @Valid @RequestBody CinemaRequestDto cinemaRequestDto){
        return VsResponseUtil.success(cinemaService.updateCinema(cinemaId,cinemaRequestDto));
    }

    @Operation(summary = "API Load All Provinces")
    @GetMapping(UrlConstant.Cinema.LOAD_ALL_PROVINCE)
    public ResponseEntity<?> loadProvince(){
        return VsResponseUtil.success(cinemaService.loadProvince());
    }

    @Operation(summary = "API Load Cinema By Province")
    @GetMapping(UrlConstant.Cinema.LOAD_CINEMA_BY_PROVINCE)
    public ResponseEntity<?> loadCinemaByProvince(@RequestParam String province){
        return VsResponseUtil.success(cinemaService.loadCinemasByProvince(province));
    }

}
