package com.doan.cinemaserver.domain.dto.cinema;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaResponsePageDto {
    private Long id;
    private String cinemaName;
    private String province;
    private String district;
    private String ward;
    private String detailAddress;
    private Integer sumRoom;
    private String hotline;
}
