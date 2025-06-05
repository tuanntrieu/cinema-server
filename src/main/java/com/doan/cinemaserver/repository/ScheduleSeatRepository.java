package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.ScheduleSeat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ScheduleSeat ss WHERE ss.schedule.id IN :scheduleIds")
    void deleteByScheduleIds(@Param("scheduleIds") List<Long> scheduleIds);

}
