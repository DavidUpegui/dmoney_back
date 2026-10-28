package com.dmoney.dmoney.tracker.infrastructure.controller.movement;

import com.dmoney.dmoney.tracker.application.movement.command.CreateMovementCommand;
import com.dmoney.dmoney.tracker.application.movement.command.EditMovementCommand;
import com.dmoney.dmoney.tracker.application.movement.response.MovementResponse;
import com.dmoney.dmoney.tracker.application.movement.usecase.CreateMovementUseCase;
import com.dmoney.dmoney.tracker.application.movement.usecase.DeleteMovementUseCase;
import com.dmoney.dmoney.tracker.application.movement.usecase.EditMovementUseCase;
import com.dmoney.dmoney.tracker.application.movement.usecase.FindAllMovementsUseCase;
import com.dmoney.dmoney.tracker.application.movement.usecase.FindMovementByIdUseCase;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
public class MovementController {

    private final CreateMovementUseCase createMovementUseCase;
    private final FindAllMovementsUseCase findAllMovementsUseCase;
    private final FindMovementByIdUseCase findMovementByIdUseCase;
    private final EditMovementUseCase editMovementUseCase;
    private final DeleteMovementUseCase deleteMovementUseCase;

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
                request.tagIds() == null ?
                        Set.of() : request.tagIds()
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

    @GetMapping
    public ResponseEntity<List<MovementResponse>> findAll(
            @RequestParam(required = false) List<String> categoryIds,
            @RequestParam(required = false) List<String> subcategoryIds,
            @RequestParam(required = false) List<String> tagIds,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) String search
    ){
        MovementFilter filter = MovementFilter.of(
                categoryIds, subcategoryIds, tagIds, type, dateFrom, dateTo, search
        );
        return ResponseEntity
                .ok()
                .body(findAllMovementsUseCase.execute(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementResponse> findById(@PathVariable String id){
        return ResponseEntity
                .ok()
                .body(findMovementByIdUseCase.execute(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovementResponse> edit(
            @PathVariable String id,
            @Valid @RequestBody EditMovementRequest request
    ){
        EditMovementCommand command = new EditMovementCommand(
                id,
                request.categoryId() == null ? null : request.categoryId().toString(),
                request.subcategoryId() == null ? null : request.subcategoryId().toString(),
                request.amount(),
                request.type(),
                request.description(),
                request.date(),
                request.tagIds() == null ?
                        null : request.tagIds()
                        .stream()
                        .map(UUID::toString)
                        .collect(Collectors.toSet())
        );

        return ResponseEntity.ok(editMovementUseCase.execute(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        deleteMovementUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
