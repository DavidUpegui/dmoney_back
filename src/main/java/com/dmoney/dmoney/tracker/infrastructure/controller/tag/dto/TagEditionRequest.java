package com.dmoney.dmoney.tracker.infrastructure.controller.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TagEditionRequest(
        @Pattern(regexp = "^(?!\\s*$).+", message = "Name cannot be blank")
        String name,
        String description
) {

}
