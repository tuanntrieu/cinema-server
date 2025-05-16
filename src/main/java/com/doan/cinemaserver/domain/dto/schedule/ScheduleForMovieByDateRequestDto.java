package com.doan.cinemaserver.domain.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleForMovieByDateRequestDto {
    private LocalDate date;
    private long movieId;
    private long cinemaId;
}
