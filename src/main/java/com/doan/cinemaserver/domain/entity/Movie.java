package com.doan.cinemaserver.domain.entity;

import com.doan.cinemaserver.domain.entity.common.DateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie extends DateAuditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id",columnDefinition = "BIGINT")
    private Long id;

    private String name;

    private String actors;

    private int duration;

    private String image;

    private String director;

    private int ageLimit;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private String language;

    private Boolean isSub;

    @ManyToMany(mappedBy = "movies",cascade = CascadeType.ALL)
    private List<MovieType> types =new ArrayList<>();

    @ManyToMany(mappedBy = "movies")
    @JsonIgnore
    private List<Room> rooms =new ArrayList<>();

    @Column(name="release_date")
    private Date releaseDate;

    @Column(name="end_date")
    private Date endDate;

    private String trailer;

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    private List<Schedule> schedules =new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    private List<Ticket> tickets =new ArrayList<>();
}
