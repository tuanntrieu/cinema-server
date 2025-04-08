package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.service.SeatService;
import lombok.RequiredArgsConstructor;

@RestApiV1
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;


}
