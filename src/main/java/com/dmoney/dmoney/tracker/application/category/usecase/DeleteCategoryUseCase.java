package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceInUseException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserProvider authProvider;
    private final MovementRepository movementRepository;

    public void execute(String categoryId){
        UserId userId = authProvider.currentUserId();
        CategoryId catId = CategoryId.from(categoryId);

        if(movementRepository.existsByUserIdAndCategoryId(userId, catId)){
            throw new ResourceInUseException("Category", "it still has movements");
        }

        boolean deleted = categoryRepository.deleteByUserIdAndId(userId, catId);
        if(!deleted){
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }
    }
}
