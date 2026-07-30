package com.tomazbr9.stock_one.dto;

import com.tomazbr9.stock_one.enums.TypeOfMovement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record RegisterStockMovementRequest(

        @NotNull(message = "A unidade é obrigatória.")
        UUID unitId,

        @NotNull(message = "O tipo de movimentação é obrigatório.")
        TypeOfMovement typeMovement,

        @NotNull(message = "A data da transação é obrigatória.")
        LocalDate transactionDate,
        
        @Size(max = 50, message = "A nota fiscal não pode ter mais de 50 caracteres.")
        String invoice,

        @Size(max = 500, message = "A observação não pode ultrapassar 500 caracteres.")
        String observation,

        @NotEmpty(message = "A movimentação deve conter pelo menos um item.")
        @Valid
        List<ItemMovementRequest> items
) {}