package com.doan.cinemaserver.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="movie_id",foreignKey = @ForeignKey(name = "FK_SCHEDULE_MOVIE"))
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id",foreignKey = @ForeignKey(name = "FK_SCHEDULE_ROOM"))
    private Room room;


}
