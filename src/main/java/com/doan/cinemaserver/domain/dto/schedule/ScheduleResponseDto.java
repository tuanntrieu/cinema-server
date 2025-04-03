package com.doan.cinemaserver.domain.dto.schedule;

import lombok.*;

import java.time.LocalTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponseDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private String movieName;

}
