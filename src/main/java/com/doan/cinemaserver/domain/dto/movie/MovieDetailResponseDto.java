package com.doan.cinemaserver.domain.dto.movie;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDetailResponseDto {
    private long id;
    private String name;
    private String actors;
    private String trailer;
    private String director;
    private int duration;
    private String description;
    private String language;
    private Boolean isSub;
    private int ageLimit;
    private String image;
    private Date releaseDate;
    private Date endDate;
    private long[] movieTypeId;
}
