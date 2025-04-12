package com.doan.cinemaserver.domain.dto.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomScheduleResponseDto {
    private Long roomId;
    private String name;
    @JsonIgnore
    private LocalDate date;
    List<TimeScheduleDto> times = new ArrayList<>();

}
