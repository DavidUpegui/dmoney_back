package com.dmoney.dmoney.tracker.application.movement.response;

import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MovementResponse(
        String movementId,
        String categoryId,
        String subcategoryId,
        BigDecimal amount,
        String type,
        String description,
        LocalDate date,
        List<UUID> tagIds
) {
    public static MovementResponse from(Movement domain){
        return new MovementResponse(
                domain.movementId().value().toString(),
                domain.catId().value().toString(),
                domain.subcatId().value().toString(),
                domain.amount().value(),
                domain.type().toString(),
                domain.description().value(),
                domain.date(),
                domain.tags().stream().map(TagId::value).toList()
        );
    }
}
