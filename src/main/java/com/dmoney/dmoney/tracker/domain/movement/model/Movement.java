package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Movement {
    final MovementId movementId;
    final UserId userId;
    CategoryId catId;
    SubcategoryId subcatId;
    Amount amount;
    MovementType type;
    MovementDescription description;
    LocalDate date;
    Set<TagId> tags = new HashSet<TagId>() {
    };


    private Movement(
            MovementId movementId,
            UserId userId,
            CategoryId catId,
            SubcategoryId subcatId,
            Amount amount,
            MovementType movementType,
            MovementDescription description,
            LocalDate date,
            Set<TagId> tags
            ){
        this.movementId = Objects.requireNonNull(movementId);
        this.userId = Objects.requireNonNull(userId);
        this.catId = Objects.requireNonNull(catId);
        this. subcatId = Objects.requireNonNull(subcatId);
        this. amount = Objects.requireNonNull(amount);
        this.type = Objects.requireNonNull(movementType);
        this.date = Objects.requireNonNull(date);
        this.description = Objects.requireNonNull(description);
        this.tags.addAll(tags);
    }

    public static Movement create(
            UserId userId,
            CategoryId catId,
            SubcategoryId subcatId,
            Amount amount,
            MovementType movementType,
            MovementDescription description,
            LocalDate date,
            Set<TagId> tags
    ){
        return new Movement(
                MovementId.newId(),
                userId,
                catId,
                subcatId,
                amount,
                movementType,
                description,
                date,
                tags
        );
    }

    public static Movement rehydrate(
            MovementId id,
            UserId userId,
            CategoryId catId,
            SubcategoryId subcatId,
            Amount amount,
            MovementType movementType,
            MovementDescription description,
            LocalDate date,
            Set<TagId> tags
    ){
        return new Movement(
                id,
                userId,
                catId,
                subcatId,
                amount,
                movementType,
                description,
                date,
                tags
        );
    }


    public MovementId movementId(){
        return this.movementId;
    }
    public UserId userId(){
        return this.userId;
    }
    public CategoryId catId(){
        return this.catId;
    }
    public SubcategoryId subcatId(){
        return this.subcatId;
    }
    public Amount amount(){
        return this.amount;
    }
    public MovementType type(){
        return this.type;
    }
    public MovementDescription description(){
        return this.description;
    }
    public LocalDate date(){
        return this.date;
    }
    public Set<TagId> tags(){
        return Collections.unmodifiableSet(this.tags);
    }
}
