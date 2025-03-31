package com.doan.cinemaserver.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private long id;

    @Column(name="schedule_time")
    private LocalDateTime scheduleTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "movie_schedule",
            joinColumns = @JoinColumn(name = "schedule_id", referencedColumnName = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "movie_id"))
    @JsonIgnore
    private List<Movie> movies =new ArrayList<>();
}
