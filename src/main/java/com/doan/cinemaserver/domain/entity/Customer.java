package com.doan.cinemaserver.domain.entity;

import com.doan.cinemaserver.constant.Gender;
import com.doan.cinemaserver.domain.entity.common.DateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends DateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name="FK_CUSTOMER_USER"),nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @JsonIgnore
    List<Ticket> tickets = new ArrayList<>();
}
