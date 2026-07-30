package com.tomazbr9.stock_one.dto;

import com.tomazbr9.stock_one.enums.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateUserRequest(

        @NotNull(message = "Numero da matricula é obrigatória")
        Integer matriculation,

        @Email(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatório")
        String password,

        UUID unitId,
        RoleName role
) {
}
