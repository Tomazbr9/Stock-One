package com.tomazbr9.stock_one.repository;

import com.tomazbr9.stock_one.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UnitRepository extends JpaRepository<Unit, UUID> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByIsCentralTrue();
}
