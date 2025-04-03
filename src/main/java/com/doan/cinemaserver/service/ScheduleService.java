package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleRequestDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleResponseDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleSearchRequestDto;
import com.doan.cinemaserver.domain.entity.Schedule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduleService {
    public CommonResponseDto createSchedule(ScheduleRequestDto requestDto);
    public CommonResponseDto deleteSchedule(Long scheduleId);
    public List<ScheduleResponseDto> searchSchedule(ScheduleSearchRequestDto requestDto);

}
