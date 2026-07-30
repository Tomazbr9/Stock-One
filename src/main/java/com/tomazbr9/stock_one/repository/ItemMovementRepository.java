package com.tomazbr9.stock_one.repository;

import com.tomazbr9.stock_one.entity.ItemMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemMovementRepository extends JpaRepository<ItemMovement, UUID> {
}
