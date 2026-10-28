package com.dmoney.dmoney.tracker.infrastructure.persistence.movement;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.movement.model.*;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MovementPersistenceMapper {

    public static Movement toDomain(MovementEntity entity){
        return Movement.rehydrate(
                MovementId.fromUUID(entity.getId()),
                UserId.fromUUID(entity.getUserId()),
                CategoryId.from(entity.getCatId().toString()),
                SubcategoryId.from(entity.getSubcatId().toString()),
                Amount.from(entity.getAmount()),
                entity.getType(),
                MovementDescription.from(entity.getDescription()),
                entity.getDate(),
                entity.getTags().stream()
                        .map(id -> TagId.from(id.toString()))
                        .collect(Collectors.toSet())
        );
    }

    public static MovementEntity toEntity(Movement domain){
        return new MovementEntity(
                domain.movementId().value(),
                domain.userId().value(),
                domain.catId().value(),
                domain.subcatId().value(),
                domain.amount().value(),
                domain.type(),
                domain.description().value(),
                domain.date(),
                domain.tags().stream().map(TagId::value).collect(Collectors.toSet())
        );
    }
}
