package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto;
import com.doan.cinemaserver.domain.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    @Query(value ="select distinct province from cinemas",nativeQuery = true)
    List<String> loadProvince();

    @Query(value = "select new com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto(c.id,c.cinemaName,c.province) from Cinema c where c.province =?1",nativeQuery = false)
    List<CinemaResponseDto> loadNameCinemaByProvince(String province);

    @Query("select new com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto(c.id,c.cinemaName,c.province) from Cinema c")
    List<CinemaResponseDto> loadAllCinemas();

}
