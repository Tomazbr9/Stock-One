package com.tomazbr9.stock_one.repository;

import com.tomazbr9.stock_one.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByCode(String code);
}
