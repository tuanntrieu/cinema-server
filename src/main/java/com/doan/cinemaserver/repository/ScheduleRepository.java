package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    Optional<Schedule> findById(Long id);


    @Query(value = """
                SELECT s.* FROM schedule s
                INNER JOIN rooms r ON s.room_id = r.room_id
                WHERE s.movie_id = ?2
                  AND r.cinema_id = ?1
                  AND s.schedule_time >= ?3
                ORDER BY s.schedule_time ASC
            """, nativeQuery = true)
    List<Schedule> getScheduleForCinema(Long cinemaId, Long movieId, LocalDateTime nowTime);


    @Query(value = """
        SELECT COUNT(*) FROM schedule_seat ss
        JOIN schedule s ON ss.schedule_id = s.schedule_id
        JOIN movies m ON s.movie_id = m.movie_id
        WHERE ss.seat_status = 'SOLD'
          AND ((TIME(s.schedule_time) >= TIME(:slotStart) AND TIME(s.schedule_time) < TIME(:slotEnd))
          OR (TIME(ADDTIME(s.schedule_time, SEC_TO_TIME(m.duration * 60))) <= TIME(:slotEnd) 
              AND TIME(ADDTIME(s.schedule_time, SEC_TO_TIME(m.duration * 60))) > TIME(:slotStart)))
          AND MONTH(s.schedule_time) = :month
          AND YEAR(s.schedule_time) = :year
        """, nativeQuery = true)
    int countSoldSeatsInSlot(
            @Param("slotStart") String slotStart,
            @Param("slotEnd") String slotEnd,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("SELECT s FROM Schedule s JOIN s.room r " +
            "WHERE s.movie.id = :movieId AND r.cinema.id = :cinemaId " +
            "AND s.scheduleTime BETWEEN :startTime AND :endTime " +
            "ORDER BY s.scheduleTime ASC")
    List<Schedule> getScheduleForMovieByDate(
            @Param("cinemaId") Long cinemaId,
            @Param("movieId") Long movieId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);


}
