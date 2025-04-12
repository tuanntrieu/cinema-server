package com.doan.cinemaserver.domain.dto.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeScheduleDto {
    private Long id;
    private LocalTime time;

    @JsonIgnore
    private LocalDate date;
    private int countSeatAvailable;
}
