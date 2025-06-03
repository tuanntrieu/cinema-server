package com.doan.cinemaserver.domain.entity;

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

    private Integer xCoordinate;

    private Integer yCoordinate;

    private boolean isMaintained = false;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "seat_type_id")
    private SeatPrice seatType;

    @ManyToOne
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "FK_ROOM_SEAT"))
    private Room room;


    @OneToMany(mappedBy = "seat")
    @JsonIgnore
    List<ScheduleSeat> scheduleSeats = new ArrayList<>();


}
