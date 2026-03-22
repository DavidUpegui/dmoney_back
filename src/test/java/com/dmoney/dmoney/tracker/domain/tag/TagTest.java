package com.dmoney.dmoney.tracker.domain.tag;

import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class TagTest {

    @Test
    void should_create_tag_from_values(){
        TagId id = TagId.from(UUID.randomUUID().toString());
        TagName name = TagName.from("Tag Name");
        TagDescription description = TagDescription.from("Tag Description");

        Tag createdTagFrom = Tag.from(id, name, description);

        assertEquals(id, createdTagFrom.id());
        assertEquals(name, createdTagFrom.name());
        assertEquals(description, createdTagFrom.description());
    }

    @Test
    void should_create_tag(){
        TagName name = TagName.from("Created tag name");
        TagDescription description = TagDescription.from("Created tag description");

        Tag createdTag = Tag.create(name, description);

        assertNotNull(createdTag.id());
        assertEquals(name, createdTag.name());
        assertEquals(description, createdTag.description());
    }

    @Test
    void should_change_name(){
        TagId tagId = TagId.newId();
        TagDescription description = TagDescription.from("Description");
        TagName changedName = TagName.from("Changed name");
        Tag tag = Tag.from(tagId, TagName.from("Old Name"), description);

        tag.changeName(changedName);

        assertEquals(tagId, tag.id());
        assertEquals(description, tag.description());
        assertEquals(changedName, tag.name());
    }

    @Test
    void should_change_description(){
        TagId tagId = TagId.newId();
        TagName name = TagName.from("Name");
        TagDescription changedDescription = TagDescription.from("Changed description");
        Tag tag = Tag.from(tagId, name, TagDescription.from("Old description"));

        tag.changeDescription(changedDescription);

        assertEquals(tagId, tag.id());
        assertEquals(name, tag.name());
        assertEquals(changedDescription, tag.description());
    }
}
