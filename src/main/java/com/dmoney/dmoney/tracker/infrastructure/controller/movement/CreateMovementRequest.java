package com.dmoney.dmoney.tracker.infrastructure.controller.movement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record CreateMovementRequest(
        @NotNull UUID categoryId,
        @NotNull UUID subcategoryId,
        @NotNull @Positive BigDecimal amount,
        @NotBlank String type,
        @Size(max = 500) String description,
        @NotNull LocalDate date,
        Set<UUID> tags
) {}
