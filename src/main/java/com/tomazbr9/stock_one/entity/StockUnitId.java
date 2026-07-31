package com.tomazbr9.stock_one.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StockUnitId implements Serializable {

    private UUID unitId;
    private UUID productId;
}
