package com.tomazbr9.stock_one.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_items_movements")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ItemMovement {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "stock_movement_id")
    private StockMovement stockMovement;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;
}
