package com.doan.cinemaserver.domain.dto.seat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeatTypeRequestDto {
    private Long roomId;
    private int row;
}
