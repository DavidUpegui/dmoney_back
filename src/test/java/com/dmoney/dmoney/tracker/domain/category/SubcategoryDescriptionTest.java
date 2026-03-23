package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryDescription;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryDescription;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubcategoryDescriptionTest {

    @Test
    void shouldCreateDescription(){
        SubcategoryDescription description = new SubcategoryDescription("description");

        assertEquals("description", description.value());
    }

    @Test
    void shouldCreateDescriptionFromValue(){
        SubcategoryDescription description = SubcategoryDescription.from("description");

        assertEquals("description", description.value());
    }

    @Test
    void shouldCreateEmptyDescriptionWhenConstructorValueIsNull(){
        SubcategoryDescription description = new SubcategoryDescription(null);

        assertEquals("", description.value());
    }

    @Test
    void shouldThrowErrorIfValueIsGreater(){
        String tooLong = "a".repeat(256);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> new SubcategoryDescription(tooLong)
        );

        assertEquals("Subcategory description is too long", exception.getMessage());
    }

    @Test
    void shouldReturnEmptyWhenUseFromNullableWithNull(){
        SubcategoryDescription description = SubcategoryDescription.fromNullable(null);

        assertEquals("", description.value());
    }

    @Test
    void shouldReturnValueWhenUseFromNullableWithValue(){
        SubcategoryDescription description = SubcategoryDescription.fromNullable("any value");

        assertEquals("any value", description.value());
    }

    @Test
    void shouldCreateEmptyDescription(){
        SubcategoryDescription description = SubcategoryDescription.empty();

        assertEquals("", description.value());
    }
}
