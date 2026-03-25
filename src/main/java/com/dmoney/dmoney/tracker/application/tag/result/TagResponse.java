package com.dmoney.dmoney.tracker.application.tag.result;

import com.dmoney.dmoney.tracker.domain.tag.model.Tag;

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
