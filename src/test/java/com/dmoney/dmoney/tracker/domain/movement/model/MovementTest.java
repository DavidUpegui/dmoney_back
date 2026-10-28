package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovementTest {

    private UserId userId;
    private CategoryId categoryId;
    private SubcategoryId subcategoryId;
    private Set<TagId> tags;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        categoryId = CategoryId.newId();
        subcategoryId = SubcategoryId.newId();
        tags = Set.of(TagId.newId());
    }

    private Movement newMovement(){
        return Movement.create(
                userId,
                categoryId,
                subcategoryId,
                Amount.from(new BigDecimal("85.50")),
                MovementType.OUTCOME,
                MovementDescription.from("Arriendo"),
                LocalDate.of(2026, 7, 14),
                tags
        );
    }

    @Test
    void shouldCreateMovementWithGeneratedId(){
        Movement movement = newMovement();

        assertNotNull(movement.movementId());
        assertEquals(userId, movement.userId());
        assertEquals(categoryId, movement.catId());
        assertEquals(subcategoryId, movement.subcatId());
        assertEquals(new BigDecimal("85.50"), movement.amount().value());
        assertEquals(MovementType.OUTCOME, movement.type());
        assertEquals("Arriendo", movement.description().value());
        assertEquals(LocalDate.of(2026, 7, 14), movement.date());
        assertEquals(tags, movement.tags());
    }

    @Test
    void shouldRehydrateMovementWithGivenId(){
        MovementId id = MovementId.newId();
        Movement movement = Movement.rehydrate(
                id,
                userId,
                categoryId,
                subcategoryId,
                Amount.from(new BigDecimal("10")),
                MovementType.INCOME,
                MovementDescription.from("salary"),
                LocalDate.of(2026, 1, 1),
                Set.of()
        );

        assertEquals(id, movement.movementId());
    }

    @Test
    void shouldChangeCategory(){
        Movement movement = newMovement();
        CategoryId newCategoryId = CategoryId.newId();

        movement.changeCategory(newCategoryId);

        assertEquals(newCategoryId, movement.catId());
    }

    @Test
    void shouldChangeSubcategory(){
        Movement movement = newMovement();
        SubcategoryId newSubcategoryId = SubcategoryId.newId();

        movement.changeSubcategory(newSubcategoryId);

        assertEquals(newSubcategoryId, movement.subcatId());
    }

    @Test
    void shouldChangeAmount(){
        Movement movement = newMovement();

        movement.changeAmount(Amount.from(new BigDecimal("100")));

        assertEquals(new BigDecimal("100"), movement.amount().value());
    }

    @Test
    void shouldChangeType(){
        Movement movement = newMovement();

        movement.changeType(MovementType.TRANSFER);

        assertEquals(MovementType.TRANSFER, movement.type());
    }

    @Test
    void shouldChangeDescription(){
        Movement movement = newMovement();

        movement.changeDescription(MovementDescription.from("New description"));

        assertEquals("New description", movement.description().value());
    }

    @Test
    void shouldChangeDate(){
        Movement movement = newMovement();

        LocalDate newDate = LocalDate.of(2026, 8, 1);
        movement.changeDate(newDate);

        assertEquals(newDate, movement.date());
    }

    @Test
    void shouldChangeTags(){
        Movement movement = newMovement();
        Set<TagId> newTags = Set.of(TagId.newId(), TagId.newId());

        movement.changeTags(newTags);

        assertEquals(newTags, movement.tags());
        assertTrue(movement.tags().containsAll(newTags));
    }
}
