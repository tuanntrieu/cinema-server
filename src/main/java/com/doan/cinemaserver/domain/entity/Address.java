package com.doan.cinemaserver.domain.entity;

import com.doan.cinemaserver.domain.entity.common.DateAuditing;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Long id;

    private String province;
    private String district;
    private String ward;
    private String detail;


    @OneToOne(mappedBy = "address")
    private Cinema cinema;

}
