package com.dmoney.dmoney.tracker.infrastructure.persistence.tag;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.tag.Tag;
import com.dmoney.dmoney.tracker.domain.tag.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.TagId;
import com.dmoney.dmoney.tracker.domain.tag.TagName;

public class TagPersistenceMapper {

    public static TagEntity toEntity(Tag tag){
        return new TagEntity(
                tag.id().value(),
                tag.userId().value(),
                tag.name().value(),
                tag.description().value()
        );
    }

    public static Tag toDomain(TagEntity tag) {
        return Tag.rehydrate(
                UserId.fromUUID(tag.getUserId()),
                TagId.from(tag.getId().toString()),
                TagName.from(tag.getName()),
                TagDescription.from(tag.getDescription())
        );
    }
}
