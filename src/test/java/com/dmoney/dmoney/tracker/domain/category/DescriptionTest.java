package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.tracker.domain.category.model.CategoryDescription;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DescriptionTest {

    @Test
    void shouldCreateDescription(){
        CategoryDescription description = new CategoryDescription("description");

        assertEquals("description", description.value());
    }

    @Test
    void shouldCreateDescriptionFromValue(){
        CategoryDescription description = CategoryDescription.from("description");

        assertEquals("description", description.value());
    }

    @Test
    void shouldThrowErrorWhenValueIsNull(){
        assertThrows(NullPointerException.class,
                () -> new CategoryDescription(null));
    }

    @Test
    void shouldThrowErrorIfValueIsGreater(){
        String tooLong = "a".repeat(256);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CategoryDescription(tooLong)
        );

        assertEquals("Description is too long", exception.getMessage());
    }

    @Test
    void shouldReturnEmptyWhenUseFromNullableWithNull(){
        CategoryDescription description = CategoryDescription.fromNullable(null);

        assertEquals("", description.value());
    }

    @Test
    void shouldReturnValueWhenUseFromNullableWithValue(){
        CategoryDescription description = CategoryDescription.fromNullable("any value");

        assertEquals("any value", description.value());
    }

    @Test
    void shouldCreateEmptyDescription(){
        CategoryDescription description = CategoryDescription.empty();

        assertEquals("", description.value());
    }
}
