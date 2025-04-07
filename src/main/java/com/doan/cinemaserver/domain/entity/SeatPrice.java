package com.doan.cinemaserver.domain.entity;


import com.doan.cinemaserver.constant.CommonConstant;
import com.doan.cinemaserver.constant.SeatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="seat_type_price")
public class SeatPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_price_id")
    private long id;

    @Column(name="weekend_price", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long weekendPrice ;

    @Column(name="weekday_price", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long weekdayPrice;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE,mappedBy = "seatType")
    @JsonIgnore
    List<Seat> seats =new ArrayList<>();
}
