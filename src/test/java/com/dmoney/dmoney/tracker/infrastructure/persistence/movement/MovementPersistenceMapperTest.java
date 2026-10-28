package com.dmoney.dmoney.tracker.infrastructure.persistence.movement;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.movement.model.Amount;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementDescription;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementId;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementType;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovementPersistenceMapperTest {

    @Test
    void should_transform_to_entity(){
        Movement domain = Movement.rehydrate(
                MovementId.fromUUID(UUID.randomUUID()),
                UserId.newId(),
                CategoryId.newId(),
                SubcategoryId.newId(),
                Amount.from(new BigDecimal("85.50")),
                MovementType.OUTCOME,
                MovementDescription.from("Arriendo"),
                LocalDate.of(2026, 7, 14),
                Set.of(TagId.newId())
        );

        MovementEntity entity = MovementPersistenceMapper.toEntity(domain);

        assertEquals(domain.movementId().value(), entity.getId());
        assertEquals(domain.userId().value(), entity.getUserId());
        assertEquals(domain.catId().value(), entity.getCatId());
        assertEquals(domain.subcatId().value(), entity.getSubcatId());
        assertEquals(new BigDecimal("85.50"), entity.getAmount());
        assertEquals(MovementType.OUTCOME, entity.getType());
        assertEquals("Arriendo", entity.getDescription());
        assertEquals(LocalDate.of(2026, 7, 14), entity.getDate());
        assertEquals(
                domain.tags().stream().map(TagId::value).collect(Collectors.toSet()),
                entity.getTags()
        );
    }

    @Test
    void should_transform_to_domain(){
        Set<UUID> tags = Set.of(UUID.randomUUID());
        MovementEntity entity = new MovementEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("85.50"),
                MovementType.OUTCOME,
                "Arriendo",
                LocalDate.of(2026, 7, 14),
                tags
        );

        Movement domain = MovementPersistenceMapper.toDomain(entity);

        assertEquals(entity.getId(), domain.movementId().value());
        assertEquals(entity.getUserId(), domain.userId().value());
        assertEquals(entity.getCatId(), domain.catId().value());
        assertEquals(entity.getSubcatId(), domain.subcatId().value());
        assertEquals(new BigDecimal("85.50"), domain.amount().value());
        assertEquals(MovementType.OUTCOME, domain.type());
        assertEquals("Arriendo", domain.description().value());
        assertEquals(LocalDate.of(2026, 7, 14), domain.date());
        assertEquals(
                tags.stream().map(id -> TagId.from(id.toString())).collect(Collectors.toSet()),
                domain.tags()
        );
    }
}
