package com.tomazbr9.stock_one.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(

        @Email(message = "Email inválido")
        String email,
        @NotBlank(message = "Senha inválida")
        String password
) {}
