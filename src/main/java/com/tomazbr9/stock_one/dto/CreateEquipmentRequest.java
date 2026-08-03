package com.tomazbr9.stock_one.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record CreateEquipmentRequest(

        @NotBlank(message = "Codigo do equipamento é obrigatório")
        @Size(max = 50, message = "O codigo do equipamento não pode ultrapassar de 50 caracteres")
        String assetCode,

        @NotBlank(message = "Nome do equipamento é obrigatório")
        String name,

        @NotNull(message = "A categoria é obrigatoria")
        UUID categoryId,

        @Size(max = 100, message = "Numero de serie não pode ultrapassar de 100 caracteres")
        String serialNumber,

        @Size(max = 50, message = "Nome da marca do equipamento não pode ultrapassar de 100 caracteres")
        String brand,

        LocalDate acquisitionDate

) {
}
