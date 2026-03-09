package com.dmoney.dmoney.tracker.domain.category;

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
        assertThrows(IllegalArgumentException.class, () -> new CategoryName(null));
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank(){
        assertThrows(IllegalArgumentException.class, () -> new CategoryName("   "));
    }
}
