package com.doan.cinemaserver.domain.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RevenueMovieResponseDto {
    private long id;
    private String name;
    private long sumTicket;
    private long totalSeat;

    public RevenueMovieResponseDto(long id, String name, long sumTicket, long totalSeat) {
        this.id = id;
        this.name = name;
        this.sumTicket = sumTicket;
        this.totalSeat = totalSeat;
    }
}
