package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    Optional<Schedule> findById(Long id);


    @Query("select s from Schedule s join s.room r where s.movie.id = ?2 and r.cinema.id =?1 order by s.scheduleTime asc")
    List<Schedule> getScheduleForCinema(Long cinemaId, Long movieId );

}
