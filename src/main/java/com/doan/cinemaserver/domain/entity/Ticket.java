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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    private String customerName;


    private String movieName;

    @JsonIgnore
    private String movieId;

    private LocalDateTime scheduleTime;

    private String seats;

    private Long currentPrice;

    @OneToMany(mappedBy = "ticket")
    @JsonIgnore
    List<TicketCombo> ticketCombo = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

}

