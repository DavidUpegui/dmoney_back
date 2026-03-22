package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.commands.EditSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditSubcategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryLoader categoryLoader;
    private final AuthenticatedUserProvider authProvider;

    public SubcategoryResult execute(EditSubcategoryCommand command){
        UserId userId = authProvider.currentUserId();
        CategoryId categoryId = CategoryId.from(command.categoryId());
        Category category = categoryLoader.load(userId, categoryId);

        SubcategoryName subcategoryName = command.subcategoryName() == null ?
                null : SubcategoryName.from(command.subcategoryName());
        SubcategoryDescription description = command.subcategoryDescription() == null ?
                null : SubcategoryDescription.from(command.subcategoryDescription());

        Subcategory subcategory = category.editSubcategory(
                SubcategoryId.from(command.subcategoryId()),
                subcategoryName,
                description);

        categoryRepository.save(category);

        return SubcategoryResult.from(subcategory, categoryId.value().toString());
    }
}
