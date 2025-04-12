package com.doan.cinemaserver.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String name;

    @Column(name = "number_row")
    private int numberOfRow;

    @Column(name="number_col")
    private int numberOfColumn;

    @OneToMany(mappedBy = "room")
    private List<Schedule> schedules = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "cinema_id", foreignKey = @ForeignKey(name = "FK_CINEMA_ROOM"), nullable = false)
    private Cinema cinema;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    List<Seat> seats = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="room_type_id")
    @JsonIgnore
    private RoomType roomType;


    @ManyToMany
    @JoinTable(
            name = "room_movie",
            joinColumns = @JoinColumn(name = "room_id", referencedColumnName = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "movie_id"))
    private List<Movie> movies = new ArrayList<>();


}
