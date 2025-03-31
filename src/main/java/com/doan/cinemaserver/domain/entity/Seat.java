package com.doan.cinemaserver.domain.entity;

import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.constant.SeatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private long id;

    @Column(name = "seat_name")
    private String seatName;

    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,
            mappedBy = "seats")
    @JsonIgnore
    private List<SeatPrice> seatPrices = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="room_id",foreignKey = @ForeignKey(name = "FK_ROOM_SEAT"))
    private Room room;

}
