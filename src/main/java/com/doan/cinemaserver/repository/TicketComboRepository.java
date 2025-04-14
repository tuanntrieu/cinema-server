package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.TicketCombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketComboRepository  extends JpaRepository<TicketCombo, Long> {
}
