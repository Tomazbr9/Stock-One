package com.tomazbr9.stock_one.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemMovementRequest(

        @NotNull(message = "O ID do produto é obrigatório.")
        UUID productId,

        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 1, message = "A quantidade movimentada deve ser de pelo menos 1.")
        Integer quantity
) {}