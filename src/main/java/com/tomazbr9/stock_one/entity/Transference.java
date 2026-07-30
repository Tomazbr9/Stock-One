package com.tomazbr9.stock_one.entity;

import com.tomazbr9.stock_one.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "source_unit_id")
    private Unit sourceUnit;

    @ManyToOne
    @JoinColumn(name = "destination_unit_id")
    private Unit destinationUnit;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private LocalDate submissionDate;

    private LocalDate receiptDate;

    @OneToMany(mappedBy = "transference", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferItem> items;
}
