package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryNameTest {

    @Test
    void shouldCreateValidCategoryName(){
        CategoryName categoryName = new CategoryName("Food");
        assertEquals("Food", categoryName.value());
    }

    @Test
    void shouldTrimValue(){
        CategoryName categoryName = new CategoryName("    FOOD   ");

        assertEquals("FOOD", categoryName.value());
    }

    @Test
    void shouldCreateNameFromString(){
        String name = "food";

        CategoryName categoryName = CategoryName.from(name);

        assertEquals(name, categoryName.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull(){
        assertThrows(ValidationException.class, () -> new CategoryName(null));
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank(){
        assertThrows(ValidationException.class, () -> new CategoryName("   "));
    }
}
