package com.tomazbr9.stock_one.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUnitRequest(

        @NotBlank(message = "O nome da unidade é obrigatório.")
        @Size(max = 100, message = "O nome da unidade não pode ultrapassar 100 caracteres.")
        String name,

        @NotNull(message = "É obrigatório informar se a unidade é o Estoque Central.")
        Boolean isCentral

) {}