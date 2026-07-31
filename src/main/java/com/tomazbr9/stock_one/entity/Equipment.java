package com.tomazbr9.stock_one.entity;

import com.tomazbr9.stock_one.enums.EquipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_equipments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "asset_code", nullable = false, unique = true, length = 50)
    private String assetCode;

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(length = 50)
    private String brand;

    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_unit_id")
    private Unit unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EquipmentStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}