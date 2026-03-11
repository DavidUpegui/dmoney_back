package com.dmoney.dmoney.tracker.application.tag.edit;

public record TagEditionCommand(
        String id,
        String name,
        String description
) {
}
