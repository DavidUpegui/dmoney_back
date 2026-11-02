package com.dmoney.dmoney.tracker.domain.tag.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TagDescriptionTest {

    @Test
    void tag03_creates_description(){
        String description = "Tag Description";

        TagDescription tagDescription = new TagDescription(description);

        assertEquals(description, tagDescription.value());
    }

    @Test
    void tag03_creates_description_from_string(){
        String description = "Tag Description";

        TagDescription tagDescription = TagDescription.from(description);

        assertEquals(description, tagDescription.value());
    }

    @Test
    void tag03_creates_empty_description(){
        TagDescription emptyTag = TagDescription.empty();

        assertEquals("", emptyTag.value());
    }

    @Test
    void tag03_nullable_null_becomes_empty(){
        TagDescription emptyDescription = TagDescription.fromNullable(null);

        assertEquals("", emptyDescription.value());
    }

    @Test
    void tag03_creates_from_nullable(){
        String description = "Tag Description";

        TagDescription tagDescription = TagDescription.fromNullable(description);

        assertEquals(description, tagDescription.value());
    }

    @Test
    void tag03_trims_description(){
        String notTrimmedDescription = "            Description      ";

        TagDescription tagDescription = TagDescription.from(notTrimmedDescription);

        assertEquals("Description", tagDescription.value());
    }

    @Test
    void tag03_null_becomes_empty(){
        TagDescription tagDescription = new TagDescription(null);

        assertEquals("", tagDescription.value());
    }

    @Test
    void tag03_rejects_too_long_description(){
        String tooLong = "a".repeat(256);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> new TagDescription(tooLong));

        assertEquals("Tag description is too long", exception.getMessage());
    }
}
