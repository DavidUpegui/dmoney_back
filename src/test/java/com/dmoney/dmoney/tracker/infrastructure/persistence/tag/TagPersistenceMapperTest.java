package com.dmoney.dmoney.tracker.infrastructure.persistence.tag;

import com.dmoney.dmoney.auth.infrastructure.persistence.user.UserPersistenceMapper;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TagPersistenceMapperTest {

    @Test
    void should_have_private_constructor() throws Exception{
        Constructor<TagPersistenceMapper> constructor =
                TagPersistenceMapper.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    void should_transform_to_entity(){
        Tag domain = Tag.rehydrate(
                UserId.newId(),
                TagId.newId(),
                TagName.from("Name"),
                TagDescription.from("Description")
        );

        TagEntity transformed = TagPersistenceMapper.toEntity(domain);

        assertEquals(domain.id().value(), transformed.id);
        assertEquals(domain.userId().value(), transformed.userId);
        assertEquals(domain.name().value(), transformed.name);
        assertEquals(domain.description().value(), transformed.description);
    }

    @Test
    void should_transform_to_domain(){
        TagEntity entity = new TagEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Name",
                "Description"
        );

        Tag transformed = TagPersistenceMapper.toDomain(entity);

        assertEquals(entity.getId(), transformed.id().value());
        assertEquals(entity.getUserId(), transformed.userId().value());
        assertEquals(entity.getName(), transformed.name().value());
        assertEquals(entity.getDescription(), transformed.description().value());
    }
}
