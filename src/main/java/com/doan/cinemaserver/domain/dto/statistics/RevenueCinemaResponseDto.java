package com.doan.cinemaserver.domain.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class RevenueCinemaResponseDto {
    private long cinemaId;
    private String cinemaName;
    private long sumTickets;
    private long total;

    public RevenueCinemaResponseDto(long cinemaId, String cinemaName, long sumTickets, long total) {
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.sumTickets = sumTickets;
        this.total = total;
    }
}
