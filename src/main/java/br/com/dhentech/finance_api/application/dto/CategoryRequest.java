package br.com.dhentech.finance_api.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "O nome da categoria é obrigatório")
        String name,
        String description
) {}
