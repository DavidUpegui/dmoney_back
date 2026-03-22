package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.EditCategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryDescription;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.category.service.CategoryUniquenessChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryLoader categoryLoader;
    private final AuthenticatedUserProvider authProvider;
    private final CategoryUniquenessChecker uniquenessChecker;

    public CategoryResult execute(EditCategoryCommand command){
        UserId userId = authProvider.currentUserId();
        CategoryId categoryId = CategoryId.from(command.id());
        Category category = categoryLoader.load(userId, categoryId);

        if (command.name() != null) {
            uniquenessChecker.check(userId,
                    CategoryName.from(command.name()),
                    category.name());
        }

        CategoryName newName = command.name() == null ? null : CategoryName.from(command.name());
        CategoryDescription newDescription =
                command.description() == null ? null : CategoryDescription.from(command.description());


        category.edit(
                newName,
                newDescription);

        return CategoryResult.from(categoryRepository.save(category));
    }
}
