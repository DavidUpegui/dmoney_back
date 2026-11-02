package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryDescriptionTest {

    @Test
    void cat03_creates_description(){
        CategoryDescription description = new CategoryDescription("description");

        assertEquals("description", description.value());
    }

    @Test
    void cat03_creates_description_from_value(){
        CategoryDescription description = CategoryDescription.from("description");

        assertEquals("description", description.value());
    }

    @Test
    void cat03_null_becomes_empty(){
        CategoryDescription description = new CategoryDescription(null);
        assertEquals("", description.value());
    }

    @Test
    void cat03_rejects_too_long(){
        String tooLong = "a".repeat(256);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> new CategoryDescription(tooLong)
        );

        assertEquals("Category description is too long", exception.getMessage());
    }

    @Test
    void cat03_nullable_null_becomes_empty(){
        CategoryDescription description = CategoryDescription.fromNullable(null);

        assertEquals("", description.value());
    }

    @Test
    void cat03_creates_from_nullable(){
        CategoryDescription description = CategoryDescription.fromNullable("any value");

        assertEquals("any value", description.value());
    }

    @Test
    void cat03_creates_empty(){
        CategoryDescription description = CategoryDescription.empty();

        assertEquals("", description.value());
    }
}
