package com.dmoney.dmoney.tracker.infrastructure.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ApiError {

    private final int status;
    private final String error;
    private final String message;
    private final Instant timestamp;

    public static ApiError of(HttpStatus status, String message) {
        return new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                Instant.now()
        );
    }
}
