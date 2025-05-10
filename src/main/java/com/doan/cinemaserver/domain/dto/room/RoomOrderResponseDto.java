package com.doan.cinemaserver.domain.dto.room;

import com.doan.cinemaserver.domain.dto.seat.SeatResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomOrderResponseDto {
    private Long roomId;
    private String roomName;
    private String roomType;
    private Long cinemaId;
    private String cinemaName;
    private Long movieId;
    private String movieName;
    private String movieImageUrl;
    private String language;
    private String movieType;
    private Integer duration;
    private LocalDate date;
    private LocalTime time;
    private int ageLimit;
    private List<SeatResponseDto> seats =new ArrayList<>();
}
