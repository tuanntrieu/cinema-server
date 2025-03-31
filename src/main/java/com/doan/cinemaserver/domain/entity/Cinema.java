package com.doan.cinemaserver.domain.entity;

import com.doan.cinemaserver.domain.entity.common.DateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="cinemas")
public class Cinema  extends DateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id")
    private Long id;

    @Column(name="cinema_name")
    private String cinemaName;

    @OneToOne
    @JoinColumn(name="address_id",foreignKey = @ForeignKey(name="FK_CINEMA_ADDRESS"),nullable = false)
    private Address address;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Room> rooms =new ArrayList<>();


    private String hotline;


}
