package com.doan.cinemaserver.domain.dto.schedule;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomScheduleDto{
    private Long roomId;
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomScheduleDto)) return false;
        RoomScheduleDto that = (RoomScheduleDto) o;
        return Objects.equals(roomId, that.roomId) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, date);
    }
}
