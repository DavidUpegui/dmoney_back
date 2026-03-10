package com.dmoney.dmoney.tracker.infrastructure.controller.tag;

import com.dmoney.dmoney.tracker.application.tag.TagResponse;
import com.dmoney.dmoney.tracker.application.tag.create.CreateTagCommand;
import com.dmoney.dmoney.tracker.application.tag.create.CreateTagUseCase;
import com.dmoney.dmoney.tracker.infrastructure.controller.tag.dto.TagCreationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final CreateTagUseCase createTagUseCase;

    @PostMapping
    public ResponseEntity<TagResponse> createTag(
            @RequestBody @Valid TagCreationRequest request
    ) {
        TagResponse created = createTagUseCase.execute(
                new CreateTagCommand(
                        request.name(),
                        request.description()
                )
        );

        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(created);
    }
}
