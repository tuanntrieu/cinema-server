package com.doan.cinemaserver.domain.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    public class RevenueChartResponseDto {
    private String label;
    private long countTickets;
    private long total;

    public RevenueChartResponseDto(long countTickets, long total) {
        this.label = "";
        this.countTickets = countTickets;
        this.total = total;
    }
}
