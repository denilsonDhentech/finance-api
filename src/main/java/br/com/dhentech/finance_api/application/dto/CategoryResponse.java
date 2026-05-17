package br.com.dhentech.finance_api.application.dto;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String description
) {}
