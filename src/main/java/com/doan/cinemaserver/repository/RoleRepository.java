package com.doan.cinemaserver.repository;


import com.doan.cinemaserver.constant.RoleConstant;
import com.doan.cinemaserver.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsByCode(RoleConstant code);
    Optional<Role> findByCode(RoleConstant code);
}
