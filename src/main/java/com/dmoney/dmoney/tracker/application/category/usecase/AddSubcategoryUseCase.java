package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.AddSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddSubcategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserProvider authProvider;
    private final CategoryLoader categoryLoader;

    public SubcategoryResult execute(AddSubcategoryCommand command) {
        UserId userId = authProvider.currentUserId();
        CategoryId categoryId = CategoryId.from(command.categoryId());
        Category category = categoryLoader.load(userId, categoryId);

        Subcategory subcategoryAdded = category.addSubcategory(
                SubcategoryName.from(command.name()),
                SubcategoryDescription.fromNullable(command.description())
        );
        categoryRepository.save(category);
        return SubcategoryResult.from(subcategoryAdded, command.categoryId());
    }
}
