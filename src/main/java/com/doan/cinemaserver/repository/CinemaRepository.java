package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto;
import com.doan.cinemaserver.domain.entity.Cinema;
import com.doan.cinemaserver.domain.entity.Movie;
import com.doan.cinemaserver.domain.entity.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long>, JpaSpecificationExecutor<Cinema> {


    @Query(value ="select distinct province from cinemas",nativeQuery = true)
    List<String> loadProvince();

    @Query(value = "select new com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto(c.id,c.cinemaName,c.province) from Cinema c  where c.province =?1",nativeQuery = false)
    List<CinemaResponseDto> loadNameCinemaByProvince(String province);

    @Query("select new com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto(c.id,c.cinemaName,c.province) from Cinema c")
    List<CinemaResponseDto> loadAllCinemas();

    @Query(value = "select r from Cinema c Join c.rooms r where c.id = ?1",
    countQuery = "select count(r.id) from Cinema  c Join c.rooms r where c.id=?1")
    Page<Room> findRoomsByCinema(Long cinemaId, Pageable pageable);

}
