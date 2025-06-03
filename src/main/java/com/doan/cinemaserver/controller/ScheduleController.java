package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleForMovieByDateRequestDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleRequestDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleSearchByCinemaRequestDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleSearchByRoomRequestDto;
import com.doan.cinemaserver.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "API Create Schedule")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(UrlConstant.Schedule.CREATE_SCHEDULE)
    public ResponseEntity<?> createSchedule(@Valid @RequestBody ScheduleRequestDto scheduleRequestDto){
        return VsResponseUtil.success(scheduleService.createSchedule(scheduleRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Delete Schedule")
    @DeleteMapping(UrlConstant.Schedule.DELETE_SCHEDULE)
    public ResponseEntity<?> deleteSchedule(@PathVariable(name = "id") Long id){
        return VsResponseUtil.success(scheduleService.deleteSchedule(id));
    }

    //Cho Admin
    @Operation(summary = "API Get Schedule For Room")
    @PostMapping(UrlConstant.Schedule.GET_SCHEDULES_BY_ROOM)
    public ResponseEntity<?> getSchedulesForRoom(@RequestBody ScheduleSearchByRoomRequestDto scheduleRequestDto){
        return VsResponseUtil.success(scheduleService.searchSchedule(scheduleRequestDto));
    }

    //cho user
    @Operation(summary = "API Get Schedule For Movie By Cinema")
    @PostMapping(UrlConstant.Schedule.GET_SCHEDULES_FOR_MOVIE_BY_CINEMA)
    public ResponseEntity<?> getSchedulesForMovieByCinema(@RequestBody ScheduleSearchByCinemaRequestDto scheduleRequestDto){
        return VsResponseUtil.success(scheduleService.getScheduleForMovieByCinema(scheduleRequestDto));
    }

    @Operation(summary = "API Get Schedule For Movie By Date")
    @PostMapping(UrlConstant.Schedule.GET_SCHEDULES_FOR_MOVIE_BY_DATE)
    public ResponseEntity<?> getSchedulesFor(@RequestBody ScheduleForMovieByDateRequestDto scheduleRequestDto){
        return VsResponseUtil.success(scheduleService.getScheduleForMovieByDate(scheduleRequestDto));
    }




}
