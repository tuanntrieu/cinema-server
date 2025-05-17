package com.doan.cinemaserver.domain.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueChartRequestDto {
    private Long cinemaId;
    private LocalDate date;
}
