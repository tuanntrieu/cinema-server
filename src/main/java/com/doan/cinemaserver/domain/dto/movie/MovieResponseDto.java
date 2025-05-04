package com.doan.cinemaserver.domain.dto.movie;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponseDto {
    private Long id;
    private String name;
    private String description;
    private String actors;
    private int duration;
    private String type;
    private String language;
    private Date releaseDate;
    private Date endDate;
    private String image;
    private Boolean isSub;
    private String director;
}
