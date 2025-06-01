package com.doan.cinemaserver.domain.dto.movie;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieScheduleResponseDto {
    private Long id;
    private String name;
}
