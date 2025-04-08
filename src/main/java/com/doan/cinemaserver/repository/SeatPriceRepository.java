package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.constant.SeatType;
import com.doan.cinemaserver.domain.entity.SeatPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SeatPriceRepository extends JpaRepository<SeatPrice, Long> {
    Boolean existsAllBySeatType(SeatType seatType);
    Optional<SeatPrice> findBySeatType(SeatType seatType);

    @Transactional
    @Modifying
    @Query(value = "update seat_type_price set weekday_price = ?2, weekend_price = ?3 where seat_type = ?1 ",nativeQuery = true)
    void updateSeatPrice(String seatType, Long weekDayPrice,Long weekendPrice);
}
