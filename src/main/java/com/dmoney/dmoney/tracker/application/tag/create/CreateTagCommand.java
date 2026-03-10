package com.dmoney.dmoney.tracker.application.tag.create;

public record CreateTagCommand(
        String name,
        String description
) {
}
