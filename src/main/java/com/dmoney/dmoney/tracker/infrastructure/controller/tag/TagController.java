package com.dmoney.dmoney.tracker.infrastructure.controller.tag;

import com.dmoney.dmoney.tracker.application.tag.findAll.FindAllTagsUseCase;
import com.dmoney.dmoney.tracker.application.tag.TagResponse;
import com.dmoney.dmoney.tracker.application.tag.create.CreateTagCommand;
import com.dmoney.dmoney.tracker.application.tag.create.CreateTagUseCase;
import com.dmoney.dmoney.tracker.application.tag.findById.FindTagByIdUseCase;
import com.dmoney.dmoney.tracker.infrastructure.controller.tag.dto.TagCreationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/tags")
@RequiredArgsConstructor
public class TagController {

    private final CreateTagUseCase createTagUseCase;
    private final FindAllTagsUseCase findAllTagsUseCase;
    private final FindTagByIdUseCase findTagById;


    @GetMapping
    public ResponseEntity<List<TagResponse>> getAll(){
        return ResponseEntity.ok().body(findAllTagsUseCase.execute());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> findById(
            @PathVariable String id
    ){
        return ResponseEntity
                .ok()
                .body(findTagById.execute(id));
    }

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
