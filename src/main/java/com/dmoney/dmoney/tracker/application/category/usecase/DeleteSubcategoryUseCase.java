package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.DeleteSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceInUseException;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSubcategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryLoader categoryLoader;
    private final AuthenticatedUserProvider authProvider;
    private final MovementRepository movementRepository;

    public void execute(DeleteSubcategoryCommand command){
        UserId userId = authProvider.currentUserId();
        CategoryId categoryId = CategoryId.from(command.categoryId());
        Category category = categoryLoader.load(userId, categoryId);
        SubcategoryId subcategoryId = SubcategoryId.from(command.subcategoryId());

        if(movementRepository.existsByUserIdAndSubcategoryId(userId, subcategoryId)){
            throw new ResourceInUseException("Subcategory", "it still has movements");
        }

        category.deleteSubcategory(subcategoryId);

        categoryRepository.save(category);
    }
}
