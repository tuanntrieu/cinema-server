package com.doan.cinemaserver.domain.entity;

import com.doan.cinemaserver.constant.RoomTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="room_type")
@Builder
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomTypeEnum roomType;

    @Column(name = "surcharge", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long surcharge;

    @OneToMany(mappedBy = "roomType")
    @JsonIgnore
    private List<Room> rooms;
}
