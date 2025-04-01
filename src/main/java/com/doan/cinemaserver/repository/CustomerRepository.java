package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
