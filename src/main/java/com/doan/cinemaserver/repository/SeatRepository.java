package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.domain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findById(Long seatId);

    @Query(value = "with tmp as(" +
            "select distinct ss.* " +
            "from movies v " +
            "inner join schedule s on s.movie_id = v.movie_id " +
            "inner join room_movie rm on v.movie_id = rm.movie_id "  +
            "inner join rooms r on r.room_id = rm.room_id " +
            "inner join cinemas c on c.cinema_id = r.cinema_id " +
            "inner join schedule_seat ss on ss.schedule_id = s.schedule_id " +
            "where ss.status = ?3 and s.schedule_id = ?2 and c.cinema_id = ?1 ) " +
            "select count(*) from tmp",nativeQuery = true)
    int countSeatByStatus(Long cinemaId, Long scheduleId, String seatStatus);
}
