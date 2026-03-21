package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.commands.CreateCategoryCommand;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.domain.category.*;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final CategoryRepository repository;

    public CategoryResult execute(CreateCategoryCommand command) {
        CategoryName name = CategoryName.from(command.name());

        if (repository.existsByNameIgnoreCase(name)) {
            throw new ResourceAlreadyExistsException("Category", "name", name.value());
        }

        Category category = new Category(
                CategoryId.newId(),
                name,
                CategoryDescription.fromNullable(command.description())
        );
        return CategoryResult.from(repository.save(category));
    }
}
