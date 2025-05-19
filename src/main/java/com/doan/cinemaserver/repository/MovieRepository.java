package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Movie;
import com.doan.cinemaserver.domain.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    Optional<Movie> findById(Long id);


    @Query(value = "select distinct v.*  " +
            "from movies v " +
            "inner join schedule s " +
            "on s.movie_id = v.movie_id " +
            "inner join room_movie rm " +
            "on v.movie_id = rm.movie_id " +
            "inner join rooms r " +
            "on r.room_id = rm.room_id " +
            "inner join cinemas c " +
            "on c.cinema_id = r.cinema_id " +
            "where DATE(s.schedule_time) = DATE(?2)"
            +"and s.schedule_time >= CONVERT_TZ(NOW(), 'SYSTEM', 'Asia/Ho_Chi_Minh')"+
            "and c.cinema_id = ?1",
            countQuery = "select count(distinct v.movie_id) " +
                    "from movies v " +
                    "inner join schedule s " +
                    "on s.movie_id = v.movie_id " +
                    "inner join room_movie rm " +
                    "on v.movie_id = rm.movie_id " +
                    "inner join rooms r " +
                    "on r.room_id = rm.room_id " +
                    "inner join cinemas c " +
                    "on c.cinema_id = r.cinema_id " +
                    "where DATE(s.schedule_time) = DATE(?2) "
                    +"and s.schedule_time >= CONVERT_TZ(NOW(), 'SYSTEM', 'Asia/Ho_Chi_Minh')"+
                    "and c.cinema_id = ?1", nativeQuery = true)
    Page<Movie> getMoviesByDate(Long cinemaId, Date date, Pageable pageable);

    @Query(value = "select  v.*  " +
            "from movies v " +
            "where date(v.release_date) > date(?1)",
            countQuery = "select count(v.movie_id) " +
                    "from movies v " +
                   " where date(v.release_date) > date(?1)",
            nativeQuery = true)
    Page<Movie> getMoviesComingSoon(Date date, Pageable pageable);



}
