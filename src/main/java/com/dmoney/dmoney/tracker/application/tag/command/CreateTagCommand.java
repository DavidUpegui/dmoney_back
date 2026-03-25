package com.dmoney.dmoney.tracker.application.tag.command;

public record CreateTagCommand(
        String name,
        String description
) {
}
