package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.constant.RoomTypeEnum;
import com.doan.cinemaserver.domain.entity.RoomType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    Boolean existsByRoomType(RoomTypeEnum roomType);

    Optional<RoomType> findByRoomType(RoomTypeEnum roomType);

    @Modifying
    @Transactional
    @Query(value ="update room_type set surcharge = ?2 where room_type = ?1" ,nativeQuery = true)
    void updateRoomSurcharge(RoomTypeEnum roomType, Long surcharge);
}
