package com.doan.cinemaserver.domain.entity;

import com.doan.cinemaserver.constant.SeatStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="schedule_seat")
@Builder
public class ScheduleSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_seat_id")
    private long id;

    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;

    private String email;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

}
