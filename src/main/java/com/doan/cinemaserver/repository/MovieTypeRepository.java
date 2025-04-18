package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.entity.MovieType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieTypeRepository extends JpaRepository<MovieType,Long> , JpaSpecificationExecutor<MovieType> {
    Optional<MovieType> findById(Long id);
}
