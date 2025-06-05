package com.doan.cinemaserver.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "schedule_time")
    private LocalDateTime scheduleTime;

    @ManyToOne()
    @JoinColumn(name = "movie_id", foreignKey = @ForeignKey(name = "FK_SCHEDULE_MOVIE"))
    private Movie movie;

    @ManyToOne()
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "FK_SCHEDULE_ROOM"))
    private Room room;

    @OneToMany(mappedBy = "schedule")
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    @JsonIgnore
    List<ScheduleSeat> scheduleSeats = new ArrayList<>();

}
