package com.tomazbr9.stock_one.entity;

import com.tomazbr9.stock_one.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_transferences")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Transference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_unit_id", nullable = false)
    private Unit sourceUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_unit_id", nullable = false)
    private Unit destinationUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransferStatus status;

    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    @Column(name = "receipt_date")
    private LocalDate receiptDate;

    @OneToMany(mappedBy = "transference", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferItem> items;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}