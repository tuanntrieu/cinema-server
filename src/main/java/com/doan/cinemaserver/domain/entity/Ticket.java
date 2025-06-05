package com.doan.cinemaserver.domain.entity;

import com.doan.cinemaserver.domain.entity.common.DateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tickets")
@Builder
public class Ticket extends DateAuditing {
    @Id
    @Column(name = "ticket_id")
    private String id;

    private String cinemaName;

    private String addressCinema;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String customerName;

    private String customerEmail;

    private String movieName;

    private LocalDateTime scheduleTime;

    @ManyToOne()
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String roomName;

    private String seatsName;

    @ManyToOne
    @JoinColumn(name="schedule_id")
    private Schedule schedule;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="cinema_id")
    private Cinema cinema;

    private Long priceSeat;

    private Long priceCombo;

    @OneToMany(mappedBy = "ticket")
    @JsonIgnore
    List<TicketCombo> ticketCombo = new ArrayList<>();


}

