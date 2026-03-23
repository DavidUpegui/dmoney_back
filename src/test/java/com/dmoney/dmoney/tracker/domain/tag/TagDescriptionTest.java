package com.dmoney.dmoney.tracker.domain.tag;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import com.dmoney.dmoney.tracker.domain.tag.model.TagDescription;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TagDescriptionTest {

    @Test
    void should_create_tag_description_with_the_constructor(){
        String description = "Tag Description";

        TagDescription tagDescription = new TagDescription(description);

        assertEquals(description, tagDescription.value());
    }

    @Test
    void should_create_tag_description_with_the_from(){
        String description = "Tag Description";

        TagDescription tagDescription = TagDescription.from(description);

        assertEquals(description, tagDescription.value());
    }

    @Test
    void should_create_empty_tag(){
        TagDescription emptyTag = TagDescription.empty();

        assertEquals("", emptyTag.value());
    }

    @Test
    void should_create_tag_empty_description_from_nullable(){
        TagDescription emptyDescription = TagDescription.fromNullable(null);

        assertEquals("", emptyDescription.value());
    }

    @Test
    void should_create_tag_from_nullable(){
        String description = "Tag Description";

        TagDescription tagDescription = TagDescription.fromNullable(description);

        assertEquals(description, tagDescription.value());
    }

    @Test
    void should_trim_value(){
        String notTrimmedDescription = "            Description      ";

        TagDescription tagDescription = TagDescription.from(notTrimmedDescription);

        assertEquals("Description", tagDescription.value());
    }

    @Test
    void should_create_empty_description_when_description_is_null(){
        TagDescription tagDescription = new TagDescription(null);

        assertEquals("", tagDescription.value());
    }

    @Test
    void should_throw_illegal_argument_exception_when_description_is_too_large(){
        String tooLong = "a".repeat(256);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> new TagDescription(tooLong));

        assertEquals("Tag description is too long", exception.getMessage());
    }
}
