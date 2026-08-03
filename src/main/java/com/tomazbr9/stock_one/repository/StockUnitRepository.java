package com.tomazbr9.stock_one.repository;

import com.tomazbr9.stock_one.entity.StockUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockUnitRepository extends JpaRepository<StockUnit, UUID> {
    List<StockUnit> findByUnitIdAndProductIdIn(UUID unitId, List<UUID> productIds);

    List<StockUnit> findByUnitId(UUID unitId);
}
