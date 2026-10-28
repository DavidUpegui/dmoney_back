package com.dmoney.dmoney.tracker.application.movement.usecase;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.movement.command.EditMovementCommand;
import com.dmoney.dmoney.tracker.application.movement.response.MovementResponse;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.movement.model.Amount;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementDescription;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementId;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementType;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import com.dmoney.dmoney.tracker.domain.movement.service.MovementCategorizationValidator;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EditMovementUseCase {

    private final MovementRepository movementRepository;
    private final AuthenticatedUserProvider authProvider;
    private final MovementCategorizationValidator categorizationValidator;

    public MovementResponse execute(EditMovementCommand command){
        UserId userId = authProvider.currentUserId();

        Movement movement = movementRepository.findByUserIdAndId(userId, MovementId.fromString(command.id()))
                .orElseThrow(() -> new ResourceNotFoundException("Movement", "id", command.id()));

        boolean categorizationChanged = false;

        if (command.categoryId() != null) {
            movement.changeCategory(CategoryId.from(command.categoryId()));
            categorizationChanged = true;
        }
        if (command.subcategoryId() != null) {
            movement.changeSubcategory(SubcategoryId.from(command.subcategoryId()));
            categorizationChanged = true;
        }
        if (command.amount() != null) {
            movement.changeAmount(Amount.from(command.amount()));
        }
        if (command.type() != null) {
            movement.changeType(MovementType.from(command.type()));
        }
        if (command.description() != null) {
            movement.changeDescription(MovementDescription.from(command.description()));
        }
        if (command.date() != null) {
            movement.changeDate(command.date());
        }
        if (command.tags() != null) {
            Set<TagId> tags = command.tags().stream().map(TagId::from).collect(Collectors.toSet());
            movement.changeTags(tags);
            categorizationChanged = true;
        }

        if (categorizationChanged) {
            categorizationValidator.validate(userId, movement.catId(), movement.subcatId(), movement.tags());
        }

        return MovementResponse.from(movementRepository.save(movement));
    }
}
