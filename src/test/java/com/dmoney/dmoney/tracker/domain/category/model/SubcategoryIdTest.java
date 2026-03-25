package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class SubcategoryIdTest {

    @Test
    void shouldCreateSubcategoryId(){
        UUID id = UUID.randomUUID();

        SubcategoryId subcategoryId = new SubcategoryId(id);

        assertEquals(id, subcategoryId.value());
    }

    @Test
    void shouldCreateSubcategoryIdFromString(){
        UUID uuid = UUID.randomUUID();
        String stringId = uuid.toString();

        SubcategoryId subcategoryId = SubcategoryId.from(stringId);

        assertEquals(uuid, subcategoryId.value());
    }


    @Test
    void shouldThrowWhenUuidIsNull() {
        assertThrows(ValidationException.class, () -> {
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
