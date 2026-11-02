package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryTypeTest {

    @Test
    void cat04_parses_value(){
        assertEquals(CategoryType.INCOME, CategoryType.from("INCOME"));
        assertEquals(CategoryType.OUTCOME, CategoryType.from("OUTCOME"));
    }

    @Test
    void cat04_rejects_unknown(){
        assertThrows(ValidationException.class, () -> CategoryType.from("nope"));
    }

    @Test
    void cat04_rejects_lowercase(){
        assertThrows(ValidationException.class, () -> CategoryType.from("income"));
    }
}
