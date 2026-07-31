package com.tomazbr9.stock_one.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_transfer_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransferItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transference_id", nullable = false)
    private Transference transference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @Column(nullable = false)
    private Integer quantity;

}