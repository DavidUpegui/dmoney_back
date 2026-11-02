package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryColorTest {

    @Test
    void cat19_trimsColor() {
        assertEquals("amber", CategoryColor.from("  amber  ").value());
    }

    @Test
    void cat19_rejectsBlankColor() {
        assertThrows(ValidationException.class, () -> CategoryColor.from("   "));
    }

    @Test
    void cat19_rejectsColorLongerThan32() {
        assertThrows(ValidationException.class,
                () -> CategoryColor.from("a".repeat(33)));
    }

    @Test
    void cat19_acceptsColorOfExactly32() {
        assertEquals(32, CategoryColor.from("a".repeat(32)).value().length());
    }

    @Test
    void cat19_nullableColorIsNullWhenAbsentOrBlank() {
        assertNull(CategoryColor.fromNullable(null));
        assertNull(CategoryColor.fromNullable("   "));
    }
}
