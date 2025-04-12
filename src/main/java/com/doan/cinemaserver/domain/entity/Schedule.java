package com.doan.cinemaserver.domain.entity;

import com.doan.cinemaserver.constant.SeatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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

    @ManyToOne()
    @JoinColumn(name="movie_id",foreignKey = @ForeignKey(name = "FK_SCHEDULE_MOVIE"))
    private Movie movie;

    @ManyToOne()
    @JoinColumn(name="room_id",foreignKey = @ForeignKey(name = "FK_SCHEDULE_ROOM"))
    private Room room;

    @ElementCollection
    @CollectionTable(name = "schedule_seat", joinColumns = @JoinColumn(name = "schedule_id"))
    @MapKeyColumn(name = "seat_id")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Map<Long, SeatStatus> seats = new HashMap<>();

}
