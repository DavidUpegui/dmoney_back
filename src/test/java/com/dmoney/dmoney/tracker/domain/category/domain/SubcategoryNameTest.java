package com.dmoney.dmoney.tracker.domain.category.domain;


import com.dmoney.dmoney.tracker.domain.category.SubcategoryName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubcategoryNameTest {
    @Test
    void shouldCreateValidSubcategoryName(){
        SubcategoryName name = new SubcategoryName("Food");

        assertEquals("food", name.value());
    }

    @Test
    void shouldTrimAndLowerCaseValue(){
        SubcategoryName name = new SubcategoryName("   TRANSPORT   ");

        assertEquals("transport", name.value());
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
