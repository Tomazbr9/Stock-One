package com.tomazbr9.stock_one.entity;

import com.tomazbr9.stock_one.enums.EquipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_equipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Equipment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String assetCode;

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 100)
    private String serialNumber;

    @Column(length = 50)
    private  String brand;

    private LocalDate acquisitionDate;

    @ManyToOne
    @JoinColumn(name = "current_unit_id")
    private Unit unit;

    @Column(nullable = false, length = 30)
    private EquipmentStatus status;
}
