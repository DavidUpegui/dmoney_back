package com.dmoney.dmoney.tracker.application.movement.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record CreateMovementCommand(
        String categoryId,
        String subcategoryId,
        BigDecimal amount,
        String type,
        String description,
        LocalDate date,
        Set<String> tags
) {
}
