package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.ScheduleSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, Long> {
}
