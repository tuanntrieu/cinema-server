package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Schedule;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    Optional<Schedule> findById(Long id);

    @Query(value = "select count(*) from schedule s where s.schedule_time >= ?1 and s.schedule_time < ?2 and s.movie_id=?3 and s.room_id =?4", nativeQuery = true)
    int countByScheduleTime(LocalDateTime startTime, LocalDateTime endTime, Long movie_id, Long room_id);

}
