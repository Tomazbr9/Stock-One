package com.tomazbr9.stock_one.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TransferItemRequest(

        UUID productId,

        UUID equipmentId,

        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
        Integer quantity
) {

    @AssertTrue(message = "Você deve informar ou um productId ou um equipmentId, nunca ambos e nunca nenhum.")
    public boolean isValidProductOrEquipment() {
        return (productId != null) ^ (equipmentId != null);
    }

}