package com.dmoney.dmoney.tracker.application.movement.usecase;

import com.dmoney.dmoney.auth.domain.user.model.User;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.movement.command.CreateMovementCommand;
import com.dmoney.dmoney.tracker.application.movement.response.MovementResponse;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.movement.model.Amount;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementDescription;
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
public class CreateMovementUseCase {

    private final MovementRepository movementRepository;
    private final AuthenticatedUserProvider authProvider;
    private final MovementCategorizationValidator categorizationValidator;

    public MovementResponse execute(CreateMovementCommand command){
        UserId userId = authProvider.currentUserId();
        CategoryId categoryId = CategoryId.from(command.categoryId());
        SubcategoryId subcategoryId = SubcategoryId.from(command.subcategoryId());
        Set<TagId> tagsId = command.tags().stream().map(TagId::from).collect(Collectors.toSet());

        categorizationValidator.validate(userId, categoryId, subcategoryId, tagsId);

        Movement toCreate = Movement.create(
                userId,
                categoryId,
                subcategoryId,
                Amount.from(command.amount()),
                MovementType.valueOf(command.type()),
                MovementDescription.from(command.description()),
                command.date(),
                tagsId
        );

        return MovementResponse.from(movementRepository.save(toCreate));
    }
}
