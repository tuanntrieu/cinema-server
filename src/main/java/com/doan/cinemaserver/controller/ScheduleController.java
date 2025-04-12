package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleRequestDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleSearchByCinemaRequestDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleSearchByRoomRequestDto;
import com.doan.cinemaserver.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "API Create Schedule")
    @PostMapping(UrlConstant.Schedule.CREATE_SCHEDULE)
    public ResponseEntity<?> createSchedule(@Valid @RequestBody ScheduleRequestDto scheduleRequestDto){
        return VsResponseUtil.success(scheduleService.createSchedule(scheduleRequestDto));
    }


    @Operation(summary = "API Delete Schedule")
    @DeleteMapping(UrlConstant.Schedule.DELETE_SCHEDULE)
    public ResponseEntity<?> deleteSchedule(@PathVariable(name = "id") Long id){
        return VsResponseUtil.success(scheduleService.deleteSchedule(id));
    }

    @Operation(summary = "API Get Schedule For Room")
    @PostMapping(UrlConstant.Schedule.GET_SCHEDULES_BY_ROOM)
    public ResponseEntity<?> getSchedulesForRoom(@RequestBody ScheduleSearchByRoomRequestDto scheduleRequestDto){
        return VsResponseUtil.success(scheduleService.searchSchedule(scheduleRequestDto));
    }

    @Operation(summary = "API Get Schedule For Cinema")
    @PostMapping(UrlConstant.Schedule.GET_SCHEDULES_BY_CINEMA)
    public ResponseEntity<?> getSchedulesForCinema(@RequestBody ScheduleSearchByCinemaRequestDto scheduleRequestDto){
        return VsResponseUtil.success(scheduleService.getScheduleForCinema(scheduleRequestDto));
    }

}
