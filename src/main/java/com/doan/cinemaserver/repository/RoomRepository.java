package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Room;
import com.doan.cinemaserver.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    Optional<Room> findById(Long roomId);

}
