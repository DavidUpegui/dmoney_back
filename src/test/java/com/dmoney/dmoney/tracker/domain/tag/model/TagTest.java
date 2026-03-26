package com.dmoney.dmoney.tracker.domain.tag.model;

import com.dmoney.dmoney.shared.domain.models.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class TagTest {

    @Test
    void should_rehydrate_tag_from_values(){
        TagId id = TagId.newId();
        UserId userId = UserId.newId();
        TagName name = TagName.from("Tag Name");
        TagDescription description = TagDescription.from("Tag Description");

        Tag rehydrateTag = Tag.rehydrate(userId,id, name, description);

        assertEquals( id, rehydrateTag.id());
        assertEquals(userId, rehydrateTag.userId());
        assertEquals(name, rehydrateTag.name());
        assertEquals(description, rehydrateTag.description());
    }

    @Test
    void should_create_tag(){
        UserId userId = UserId.newId();
        TagName name = TagName.from("Created tag name");
        TagDescription description = TagDescription.from("Created tag description");

        Tag createdTag = Tag.create(userId, name, description);

        assertNotNull(createdTag.id());
        assertEquals(userId, createdTag.userId());
        assertEquals(name, createdTag.name());
        assertEquals(description, createdTag.description());
    }

    @Test
    void should_change_name(){
        UserId userId = UserId.newId();
        TagDescription description = TagDescription.from("Description");
        TagName changedName = TagName.from("Changed name");
        Tag tag = Tag.create(userId, TagName.from("Old Name"), description);
        TagId tagId = tag.id();

        tag.changeName(changedName);

        assertEquals(userId, tag.userId());
        assertEquals(tagId, tag.id());
        assertEquals(description, tag.description());
        assertEquals(changedName, tag.name());
    }

    @Test
    void should_change_description(){
        UserId userId = UserId.newId();
        TagName name = TagName.from("Name");
        TagDescription changedDescription = TagDescription.from("Changed description");
        Tag tag = Tag.create(userId, name, TagDescription.from("Old description"));
        TagId tagId = tag.id();

        tag.changeDescription(changedDescription);

        assertEquals(tagId, tag.id());
        assertEquals(userId, tag.userId());
        assertEquals(name, tag.name());
        assertEquals(changedDescription, tag.description());
    }
}
