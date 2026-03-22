package com.dmoney.dmoney.tracker.domain.tag;

import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TagIdTest {

    @Test
    void should_create_id_from_constructor(){
        UUID uuid = UUID.randomUUID();

        TagId tagId = new TagId(uuid);

        assertEquals(uuid, tagId.value());
    }

    @Test
    void should_create_id_from_string(){
        String stringUUID = UUID.randomUUID().toString();

        TagId tagId = TagId.from(stringUUID);

        assertEquals(stringUUID, tagId.value().toString());
    }

    @Test
    void should_generate_unique_valid_ids(){
        TagId id1 = TagId.newId();
        TagId id2 = TagId.newId();

        assertNotNull(id1.value());
        assertNotNull(id2.value());
        assertNotEquals(id1, id2);
    }

    @Test
    void should_throw_null_pointer_exception_when_id_is_null(){
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new TagId(null));

        assertEquals("Tag id cannot be null", exception.getMessage());
    }
}
