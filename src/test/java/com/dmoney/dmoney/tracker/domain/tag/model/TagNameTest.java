package com.dmoney.dmoney.tracker.domain.tag.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TagNameTest {

    @Test
    void tag02_creates_name_from_constructor(){
        String name = "Name";

        TagName tagName = new TagName(name);

        assertEquals(name, tagName.value());
    }

    @Test
    void tag02_creates_name_from_string(){
        String name = "Name";

        TagName tagName = TagName.from(name);

        assertEquals(name, tagName.value());
    }

    @Test
    void tag02_trims_name(){
        String name = "           Name       ";

        TagName tagName = TagName.from(name);

        assertEquals("Name", tagName.value());
    }

    @Test
    void tag02_rejects_null_name(){
        ValidationException exception =  assertThrows(ValidationException.class,
                () -> new TagName(null));

        assertEquals("Tag name cannot be null or blank.", exception.getMessage());
    }

    @Test
    void tag02_rejects_blank_name(){
        ValidationException exception =  assertThrows(ValidationException.class,
                () -> new TagName(""));

        assertEquals("Tag name cannot be null or blank.", exception.getMessage());
    }

    @Test
    void tag02_rejects_too_long_name(){
        assertThrows(ValidationException.class, () -> TagName.from("a".repeat(256)));
    }
}
