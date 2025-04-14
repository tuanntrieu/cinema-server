package com.doan.cinemaserver.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket_combo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCombo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "combo_id", nullable = false)
    private Combo combo;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    private Long currentPrice;
}
