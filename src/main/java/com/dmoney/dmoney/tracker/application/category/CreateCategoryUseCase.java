package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.CreateCategoryCommand;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.*;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final CategoryRepository repository;
    private final AuthenticatedUserProvider authenticatedProvider;

    public CategoryResult execute(CreateCategoryCommand command) {
        UserId userId = authenticatedProvider.currentUserId();
        CategoryName name = CategoryName.from(command.name());

        if (repository.existsByUserIdAndNameIgnoreCase(userId, name)) {
            throw new ResourceAlreadyExistsException("Category", "name", name.value());
        }

        Category category = Category.create(
                userId,
                name,
                CategoryDescription.fromNullable(command.description())
        );
        return CategoryResult.from(repository.save(category));
    }
}
