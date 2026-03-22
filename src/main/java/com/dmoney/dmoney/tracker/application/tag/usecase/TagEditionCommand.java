package com.dmoney.dmoney.tracker.application.tag.usecase;

public record TagEditionCommand(
        String id,
        String name,
        String description
) {
}
