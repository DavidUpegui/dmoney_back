package com.dmoney.dmoney.tracker.domain.category;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DescriptionTest {

    @Test
    void shouldCreateDescription(){
        Description description = new Description("description");

        assertEquals("description", description.value());
    }

    @Test
    void shouldCreateDescriptionFromValue(){
        Description description = Description.from("description");

        assertEquals("description", description.value());
    }

    @Test
    void shouldThrowErrorWhenValueIsNull(){
        assertThrows(NullPointerException.class,
                () -> new Description(null));
    }

    @Test
    void shouldThrowErrorIfValueIsGreater(){
        String tooLong = "a".repeat(256);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Description(tooLong)
        );

        assertEquals("Description is too long", exception.getMessage());
    }

    @Test
    void shouldReturnEmptyWhenUseFromNullableWithNull(){
        Description description = Description.fromNullable(null);

        assertEquals("", description.value());
    }

    @Test
    void shouldReturnValueWhenUseFromNullableWithValue(){
        Description description = Description.fromNullable("any value");

        assertEquals("any value", description.value());
    }

    @Test
    void shouldCreateEmptyDescription(){
        Description description = Description.empty();

        assertEquals("", description.value());
    }
}
