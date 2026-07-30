package com.tomazbr9.stock_one.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_stock_unit")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StockUnit {

    @EmbeddedId
    private StockUnitId id;

    @ManyToOne
    @MapsId("unitId")
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer currentQuantity;

    private Integer minimumStock;

    private String location;

}
