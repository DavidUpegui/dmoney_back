package com.dmoney.dmoney.tracker.application.tag.command;

public record TagEditionCommand(
        String id,
        String name,
        String description
) {
}
