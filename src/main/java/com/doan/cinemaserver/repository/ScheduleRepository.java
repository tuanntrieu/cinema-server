package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
