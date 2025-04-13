package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.domain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findById(Long seatId);

    @Query("select s from Seat s where s.xCoordinate = ?1 and s.yCoordinate = ?2 and s.room.id = ?3")
    Optional<Seat> findByCoordinate(int x, int y,Long roomId);

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


    @Modifying
    @Transactional
    @Query(value = "update schedule_seat s set s.status = ?3 where s.schedule_id = ?1 and s.seat_id = ?2 ",nativeQuery = true)
    void updateSeatStatus(Long scheduleId,Long seatId, SeatStatus seatStatus);

    @Query("select min(s.yCoordinate) from Seat s where s.xCoordinate = ?2 and s.room.id =?1")
    int getMinYCoordinate(Long roomId, int xCoordinate);

    @Query("select max(s.yCoordinate) from Seat s where s.xCoordinate = ?2 and s.room.id =?1")
    int getMaxYCoordinate(Long roomId, int xCoordinate);

}
