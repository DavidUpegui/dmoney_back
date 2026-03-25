package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class CategoryIdTest {

    @Test
    void shouldCreateCategoryId(){
        UUID id = UUID.randomUUID();
        CategoryId categoryId = new CategoryId(id);
        assertEquals(id, categoryId.value());
    }

    @Test
    void shouldCreateCategoryIdFromString(){
        UUID uuid = UUID.randomUUID();
        String stringId = uuid.toString();

        CategoryId categoryId = CategoryId.from(stringId);

        assertEquals(uuid, categoryId.value());
    }

    @Test
    void shouldThrowWhenIdIsNull(){
        assertThrows(ValidationException.class, ()-> new CategoryId(null));
    }

    @Test
    void shouldGenerateDifferentIds(){
        CategoryId id1 = CategoryId.newId();
        CategoryId id2 = CategoryId.newId();

        assertNotEquals(id1.value(), id2.value());
    }


}
