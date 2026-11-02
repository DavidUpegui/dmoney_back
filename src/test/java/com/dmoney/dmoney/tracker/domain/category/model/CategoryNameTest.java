package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryNameTest {

    @Test
    void cat02_creates_name(){
        CategoryName categoryName = new CategoryName("Food");
        assertEquals("Food", categoryName.value());
    }

    @Test
    void cat02_trims_name(){
        CategoryName categoryName = new CategoryName("    FOOD   ");

        assertEquals("FOOD", categoryName.value());
    }

    @Test
    void cat02_creates_from_string(){
        String name = "food";

        CategoryName categoryName = CategoryName.from(name);

        assertEquals(name, categoryName.value());
    }

    @Test
    void cat02_rejects_null(){
        assertThrows(ValidationException.class, () -> new CategoryName(null));
    }

    @Test
    void cat02_rejects_blank(){
        assertThrows(ValidationException.class, () -> new CategoryName("   "));
    }

    @Test
    void cat02_rejects_too_long(){
        assertThrows(ValidationException.class, () -> new CategoryName("a".repeat(256)));
    }
}
