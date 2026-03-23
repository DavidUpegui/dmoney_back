package com.dmoney.dmoney.tracker.domain.tag;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TagNameTest {

    @Test
    void should_create_name_from_constructor(){
        String name = "Name";

        TagName tagName = new TagName(name);

        assertEquals(name, tagName.value());
    }

    @Test
    void should_create_name_from_string(){
        String name = "Name";

        TagName tagName = TagName.from(name);

        assertEquals(name, tagName.value());
    }

    @Test
    void should_trim_value(){
        String name = "           Name       ";

        TagName tagName = TagName.from(name);

        assertEquals("Name", tagName.value());
    }

    @Test
    void should_throw_illegal_argument_exception_when_null(){
        ValidationException exception =  assertThrows(ValidationException.class,
                () -> new TagName(null));

        assertEquals("Tag name cannot be null or blank.", exception.getMessage());
    }

    @Test
    void should_throw_illegal_argument_exception_when_value_is_blank(){
        ValidationException exception =  assertThrows(ValidationException.class,
                () -> new TagName(""));

        assertEquals("Tag name cannot be null or blank.", exception.getMessage());
    }
}
