package com.dmoney.dmoney.tracker.application.tag;

import com.dmoney.dmoney.tracker.domain.tag.Tag;

public record TagResponse(
        String id,
        String name,
        String description
) {

    public static TagResponse from(Tag tag){
        return new TagResponse(
                tag.id().value().toString(),
                tag.name().value(),
                tag.description().value()
        );
    }
}
