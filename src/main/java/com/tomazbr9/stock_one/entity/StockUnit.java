package com.tomazbr9.stock_one.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_stock_units")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StockUnit {

    @EmbeddedId
    private StockUnitId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("unitId")
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "current_quantity", nullable = false)
    @Builder.Default
    private Integer currentQuantity = 0;

    @Column(name = "minimum_stock", nullable = false)
    @Builder.Default
    private Integer minimumStock = 0;

    @Column(length = 100)
    private String location;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}