package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.AddSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddSubcategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryLoader categoryLoader;

    public SubcategoryResult execute(AddSubcategoryCommand command) {
        CategoryId categoryId = CategoryId.from(command.categoryId());
        Category category = categoryLoader.load(categoryId);

        Subcategory subcategoryAdded = category.addSubcategory(
                SubcategoryName.from(command.name()),
                SubcategoryDescription.fromNullable(command.description())
        );
        categoryRepository.save(category);
        return SubcategoryResult.from(subcategoryAdded, command.categoryId());
    }
}
