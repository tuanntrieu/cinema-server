package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String>, JpaSpecificationExecutor<Ticket> {
    public boolean existsById(String id);
    public Optional<Ticket> findById(String id);
}
