package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryIconTest {

    @Test
    void cat18_trimsIcon() {
        assertEquals("shopping-basket",
                CategoryIcon.from("  shopping-basket  ").value());
    }

    @Test
    void cat18_rejectsBlankIcon() {
        assertThrows(ValidationException.class, () -> CategoryIcon.from("   "));
    }

    @Test
    void cat18_rejectsIconLongerThan48() {
        assertThrows(ValidationException.class,
                () -> CategoryIcon.from("a".repeat(49)));
    }

    @Test
    void cat18_acceptsIconOfExactly48() {
        assertEquals(48, CategoryIcon.from("a".repeat(48)).value().length());
    }

    @Test
    void cat18_nullableIconIsNullWhenAbsentOrBlank() {
        assertNull(CategoryIcon.fromNullable(null));
        assertNull(CategoryIcon.fromNullable("   "));
    }
}
