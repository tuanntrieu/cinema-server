package com.doan.cinemaserver.domain.dto.cinema;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaResponseDto {
    private Long id;
    private String name;
    private String province;
}
