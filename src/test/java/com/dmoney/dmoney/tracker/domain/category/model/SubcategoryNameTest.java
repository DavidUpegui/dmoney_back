package com.dmoney.dmoney.tracker.domain.category.model;


import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubcategoryNameTest {
    @Test
    void cat12_creates_subcategory_name(){
        SubcategoryName name = new SubcategoryName("Food");

        assertEquals("Food", name.value());
    }

    @Test
    void cat12_trims_subcategory_name(){
        SubcategoryName name = new SubcategoryName("   TRANSPORT   ");

        assertEquals("TRANSPORT", name.value());
    }

    @Test
    void cat12_creates_subcategory_name_from_value(){
        String name = "food";

        SubcategoryName subcategoryName = SubcategoryName.from(name);

        assertEquals(name, subcategoryName.value());
    }

    @Test
    void cat12_rejects_null_subcategory_name(){
         assertThrows(ValidationException.class, () -> new SubcategoryName(null));
    }

    @Test
    void cat12_rejects_blank_subcategory_name(){
        assertThrows(ValidationException.class, () -> new SubcategoryName("   "));
    }

    @Test
    void cat12_rejects_too_long_subcategory_name(){
        assertThrows(ValidationException.class, () -> new SubcategoryName("a".repeat(256)));
    }

}
