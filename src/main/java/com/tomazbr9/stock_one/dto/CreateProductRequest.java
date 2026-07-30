package com.tomazbr9.stock_one.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateProductRequest(

        @NotBlank(message = "O código do produto é obrigatório.")
        @Size(max = 50, message = "O código do produto não pode ultrapassar 50 caracteres.")
        String code,

        @NotBlank(message = "O nome do produto é obrigatório.")
        @Size(max = 150, message = "O nome do produto não pode ultrapassar 150 caracteres.")
        String name,

        @NotNull(message = "A categoria é obrigatória.")
        UUID categoryId,

        @NotBlank(message = "A unidade de medida é obrigatória.")
        @Size(max = 20, message = "A unidade de medida não pode ultrapassar 20 caracteres.")
        String unitMeasurement
) {}