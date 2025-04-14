package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {

    Optional<Combo> findById(Long id);

}
