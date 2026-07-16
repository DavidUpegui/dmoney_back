package com.dmoney.dmoney.tracker.infrastructure.controller.movement;

import com.dmoney.dmoney.tracker.application.movement.command.CreateMovementCommand;
import com.dmoney.dmoney.tracker.application.movement.response.MovementResponse;
import com.dmoney.dmoney.tracker.application.movement.usecase.CreateMovementUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
public class MovementController {

    private final CreateMovementUseCase createMovementUseCase;

    @PostMapping
    public ResponseEntity<MovementResponse> create(
            @Valid  @RequestBody CreateMovementRequest request
    ){
        CreateMovementCommand cmd = new CreateMovementCommand(
                request.categoryId().toString(),
                request.subcategoryId().toString(),
                request.amount(),
                request.type(),
                request.description(),
                request.date(),
                request.tags() == null ?
                        Set.of() : request.tags()
                                .stream()
                                .map(UUID::toString)
                                .collect(Collectors.toSet())
        );

        MovementResponse response = createMovementUseCase.execute(cmd);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.movementId())
                .toUri();


        return ResponseEntity.created(location).body(response);
    }
}
