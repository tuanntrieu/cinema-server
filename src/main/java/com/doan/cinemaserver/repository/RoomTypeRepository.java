package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.constant.RoomTypeEnum;
import com.doan.cinemaserver.domain.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    Boolean existsByRoomType(RoomTypeEnum roomType);


    Optional<RoomType> findByRoomType(RoomTypeEnum roomType);
}
