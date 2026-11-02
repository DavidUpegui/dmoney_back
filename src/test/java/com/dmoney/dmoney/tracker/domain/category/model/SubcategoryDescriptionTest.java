package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubcategoryDescriptionTest {

    @Test
    void cat12_creates_subcategory_description(){
        SubcategoryDescription description = new SubcategoryDescription("description");

        assertEquals("description", description.value());
    }

    @Test
    void cat12_creates_subcategory_description_from_value(){
        SubcategoryDescription description = SubcategoryDescription.from("description");

        assertEquals("description", description.value());
    }

    @Test
    void cat12_subcategory_null_becomes_empty(){
        SubcategoryDescription description = new SubcategoryDescription(null);

        assertEquals("", description.value());
    }

    @Test
    void cat12_rejects_too_long_subcategory_description(){
        String tooLong = "a".repeat(256);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> new SubcategoryDescription(tooLong)
        );

        assertEquals("Subcategory description is too long", exception.getMessage());
    }

    @Test
    void cat12_subcategory_nullable_null_becomes_empty(){
        SubcategoryDescription description = SubcategoryDescription.fromNullable(null);

        assertEquals("", description.value());
    }

    @Test
    void cat12_creates_subcategory_description_from_nullable(){
        SubcategoryDescription description = SubcategoryDescription.fromNullable("any value");

        assertEquals("any value", description.value());
    }

    @Test
    void cat12_creates_empty_subcategory_description(){
        SubcategoryDescription description = SubcategoryDescription.empty();

        assertEquals("", description.value());
    }
}
