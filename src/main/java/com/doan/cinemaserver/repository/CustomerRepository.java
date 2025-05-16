package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c where c.user.email = ?1")
    Optional<Customer> findByEmail(String email);

    @Query(value = "select count(*) from customers c where date(c.created_date) = date(:date)", nativeQuery = true)
    int countCustomerByDate(@Param("date") LocalDate date);

    @Query(value = "select count(*) from customers c where yearweek(c.created_date) = yearweek(:date)", nativeQuery = true)
    int countCustomerByWeek(@Param("date") LocalDate date);

    @Query(value = "select count(*) from customers c where month(c.created_date) = month(:date) and year(t.created_date) = year(:date)", nativeQuery = true)
    int countCustomerByMonth(@Param("date") LocalDate date);


}
