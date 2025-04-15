package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.ComboDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboDetailRepository extends JpaRepository<ComboDetail, Long> {
}
