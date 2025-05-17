package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String>, JpaSpecificationExecutor<Ticket> {
    public boolean existsById(String id);

    public Optional<Ticket> findById(String id);

    @Query(value = "select count(*) from tickets t where date(t.created_date) = date(:date)", nativeQuery = true)
    int countTicketByDate(@Param("date") LocalDate date);

    @Query(value = "select count(*) from tickets t where yearweek(t.created_date) = yearweek(:date)", nativeQuery = true)
    int countTicketByWeek(@Param("date") LocalDate date);

    @Query(value = "select count(*) from tickets t where  month(t.created_date) = month(:date) and year(t.created_date) = year(:date)", nativeQuery = true)
    int countTicketByMonth(@Param("date") LocalDate date);

    @Query(value = "select coalesce(sum(t.price_combo),0)+coalesce(sum(t.price_seat),0) " +
            "from tickets t where date(t.created_date) = date(:date)"
            , nativeQuery = true)
    long sumTotalByDate(@Param("date") LocalDate date);

    @Query(value = "select coalesce(sum(t.price_combo),0)+coalesce(sum(t.price_seat),0) " +
            "from tickets t where yearweek(t.created_date) = yearweek(:date)"
            , nativeQuery = true)
    long sumTotalByWeek(@Param("date") LocalDate date);

    @Query(value = "select coalesce(sum(t.price_combo),0)+coalesce(sum(t.price_seat),0) " +
            "from tickets t where month(t.created_date) = month(:date) and year(t.created_date) = year(:date)"
            , nativeQuery = true)
    long sumTotalByMonth(@Param("date") LocalDate date);



}
