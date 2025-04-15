package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findById(Long id);
    List<Food> findAll();
    Page<Food> findAll(Pageable pageable);
}
