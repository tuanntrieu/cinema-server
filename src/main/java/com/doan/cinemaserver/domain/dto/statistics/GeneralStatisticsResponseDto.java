package com.doan.cinemaserver.domain.dto.statistics;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralStatisticsResponseDto {
    private long total;
    private double rate;
}
