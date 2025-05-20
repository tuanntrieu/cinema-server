package com.doan.cinemaserver.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="combo_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="combo_detail_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "combo_id")
    private Combo combo;

    @ManyToOne()
    @JoinColumn(name = "food_id")
    private Food food;

    private Integer quantity;


}
