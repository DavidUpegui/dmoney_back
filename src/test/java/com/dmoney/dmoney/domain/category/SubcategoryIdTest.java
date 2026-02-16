package com.dmoney.dmoney.domain.category;

import com.dmoney.dmoney.tracker.domain.category.SubcategoryId;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class SubcategoryIdTest {

    @Test
    void shouldCreateSubcategoryId(){
        UUID id = UUID.randomUUID();

        SubcategoryId subcategoryId = new SubcategoryId(id);

        assertEquals(id, subcategoryId.value());
    }

    @Test
    void shouldThrowWhenUuidIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new SubcategoryId(null);
        });
    }

    @Test
    void shouldGenerateDifferentIds() {
        SubcategoryId id1 = SubcategoryId.newId();
        SubcategoryId id2 = SubcategoryId.newId();

        assertNotEquals(id1, id2);
    }
}
