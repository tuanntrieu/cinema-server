package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.schedule.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduleService {
    public CommonResponseDto createSchedule(ScheduleRequestDto requestDto);
    public CommonResponseDto deleteSchedule(Long scheduleId);
    public List<ScheduleForRoomResponseDto> searchSchedule(ScheduleSearchByRoomRequestDto requestDto);
    public List<ScheduleForCinemaResponseDto> getScheduleForCinema(ScheduleSearchByCinemaRequestDto requestDto);

}
