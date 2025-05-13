package com.doan.cinemaserver.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "food",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ComboDetail> comboDetails = new ArrayList<>();
}
