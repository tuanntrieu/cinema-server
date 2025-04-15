package com.doan.cinemaserver.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "combo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="combo_id")
    private Long id;

    private String name;

    private String image;

    private Long price;

    private String description;

    @OneToMany(mappedBy = "combo")
    @JsonIgnore
    private List<TicketCombo> ticketCombo = new ArrayList<>();

    @OneToMany(mappedBy = "combo",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ComboDetail> comboDetail = new ArrayList<>();

}
