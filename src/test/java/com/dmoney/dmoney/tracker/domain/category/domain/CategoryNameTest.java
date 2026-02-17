package com.dmoney.dmoney.tracker.domain.category.domain;

import com.dmoney.dmoney.tracker.domain.category.CategoryName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryNameTest {

    @Test
    void shouldCreateValidCategoryName(){
        CategoryName categoryName = new CategoryName("Food");
        assertEquals("food", categoryName.value());
    }

    @Test
    void shouldTrimAndLowerCaseValue(){
        CategoryName categoryName = new CategoryName("    FOOD   ");

        assertEquals("food", categoryName.value());
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
