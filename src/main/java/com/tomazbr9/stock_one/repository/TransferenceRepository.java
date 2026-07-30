package com.tomazbr9.stock_one.repository;

import com.tomazbr9.stock_one.entity.Transference;
import com.tomazbr9.stock_one.entity.Unit;
import com.tomazbr9.stock_one.enums.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransferenceRepository extends JpaRepository<Transference, UUID> {
    List<Transference> findBySourceUnitAndStatus(Unit unit, TransferStatus status);
}
