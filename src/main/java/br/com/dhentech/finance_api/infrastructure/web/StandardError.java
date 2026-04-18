package br.com.dhentech.finance_api.infrastructure.web;

import java.time.Instant;

public record StandardError(
        Instant timestamp,
        Integer status,
        String message,
        String path
) {}
