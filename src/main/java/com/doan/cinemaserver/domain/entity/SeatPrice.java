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
@Table(name="seat_price")
public class SeatPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_price_id")
    private long id;

    @Column(name="weekend_price", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long weekendPrice = CommonConstant.ZERO_VALUE;

    @Column(name="weekday_price", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long weekdayPrice;

    @Column(name="surcharge", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long surcharge;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "seat_price_jtb",
            joinColumns = @JoinColumn(name = "seat_price_id", referencedColumnName = "seat_price_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id", referencedColumnName = "seat_id"))
    @JsonIgnore
    List<Seat> seats =new ArrayList<>();
}
