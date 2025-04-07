package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.constant.SeatType;
import com.doan.cinemaserver.domain.entity.SeatPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatPriceRepository extends JpaRepository<SeatPrice, Long> {
    Boolean existsAllBySeatType(SeatType seatType);
    Optional<SeatPrice> findBySeatType(SeatType seatType);
}
