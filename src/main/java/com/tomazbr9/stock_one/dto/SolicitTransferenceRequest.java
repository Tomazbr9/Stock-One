package com.tomazbr9.stock_one.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SolicitTransferenceRequest(

        @NotNull(message = "A unidade de origem é obrigatória.")
        UUID sourceUnitId,

        @NotNull(message = "A unidade de destino é obrigatória.")
        UUID destinationUnitId,

        @NotEmpty(message = "A transferência deve conter pelo menos um item.")
        @Valid
        List<TransferItemRequest> items
) {}