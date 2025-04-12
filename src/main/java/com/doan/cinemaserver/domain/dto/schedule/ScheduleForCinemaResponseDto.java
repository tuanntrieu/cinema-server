package com.doan.cinemaserver.domain.dto.schedule;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleForCinemaResponseDto {
    private LocalDate date;
    private List<RoomScheduleResponseDto> roomSchedules;
}
