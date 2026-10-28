package com.dmoney.dmoney.tracker.infrastructure.controller.movement;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record EditMovementRequest(
        UUID categoryId,
        UUID subcategoryId,
        @Positive BigDecimal amount,
        String type,
        @Size(max = 500) String description,
        LocalDate date,
        Set<UUID> tagIds
) {}
