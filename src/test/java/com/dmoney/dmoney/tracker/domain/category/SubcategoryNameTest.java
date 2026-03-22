package com.dmoney.dmoney.tracker.domain.category;


import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubcategoryNameTest {
    @Test
    void shouldCreateValidSubcategoryName(){
        SubcategoryName name = new SubcategoryName("Food");

        assertEquals("Food", name.value());
    }

    @Test
    void shouldTrimValue(){
        SubcategoryName name = new SubcategoryName("   TRANSPORT   ");

        assertEquals("TRANSPORT", name.value());
    }

    @Test
    void shouldCreateNameFromValue(){
        String name = "food";

        SubcategoryName subcategoryName = SubcategoryName.from(name);

        assertEquals(name, subcategoryName.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull(){
         assertThrows(IllegalArgumentException.class, () -> new SubcategoryName(null));
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank(){
        assertThrows(IllegalArgumentException.class, () -> new SubcategoryName("   "));
    }

}
