package com.dmoney.dmoney.tracker.infrastructure.persistence.tag;

import com.dmoney.dmoney.tracker.domain.tag.Tag;
import com.dmoney.dmoney.tracker.domain.tag.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.TagId;
import com.dmoney.dmoney.tracker.domain.tag.TagName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagPersistenceMapperTest {

    @Test
    void should_transform_to_entity(){
        Tag domain = Tag.from(
                TagId.newId(),
                TagName.from("Name"),
                TagDescription.from("Description")
        );

        TagEntity transformed = TagPersistenceMapper.toEntity(domain);

        assertEquals(domain.id().value(), transformed.id);
        assertEquals(domain.name().value(), transformed.name);
        assertEquals(domain.description().value(), transformed.description);
    }

    @Test
    void should_transform_to_domain(){
        TagEntity entity = new TagEntity(
                UUID.randomUUID(),
                "Name",
                "Description"
        );

        Tag transformed = TagPersistenceMapper.toDomain(entity);

        assertEquals(entity.getId(), transformed.id().value());
        assertEquals(entity.getName(), transformed.name().value());
        assertEquals(entity.getDescription(), transformed.description().value());
    }
}
