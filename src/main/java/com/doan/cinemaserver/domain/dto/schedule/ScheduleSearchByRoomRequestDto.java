package com.doan.cinemaserver.domain.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSearchByRoomRequestDto {
    private Long movieId;
   // @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Long roomId;

}
