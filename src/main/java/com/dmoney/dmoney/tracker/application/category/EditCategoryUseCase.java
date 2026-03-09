package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.commands.EditCategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.domain.category.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryLoader categoryLoader;

    public CategoryResult execute(EditCategoryCommand command){

        CategoryId categoryId = CategoryId.from(command.id());

        Category category = categoryLoader.load(categoryId);
        Description newDescription = command.description() == null ? null : Description.from(command.description());
        CategoryName newName = command.name() == null ? null : CategoryName.from(command.name());

        category.edit(
                newName,
                newDescription);

        return CategoryResult.from(categoryRepository.save(category));
    }
}
