package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.commands.EditSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.tracker.domain.category.*;
import com.dmoney.dmoney.tracker.exceptions.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditSubcategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryLoader categoryLoader;

    public SubcategoryResult execute(EditSubcategoryCommand command){
        CategoryId categoryId = CategoryId.from(command.categoryId());
        Category category = categoryLoader.load(categoryId);

        SubcategoryName subcategoryName = command.subcategoryName() == null ?
                null : SubcategoryName.from(command.subcategoryName());
        Description description = command.subcategoryDescription() == null ?
                null : Description.from(command.subcategoryName());

        Subcategory subcategory = category.editSubcategory(
                SubcategoryId.from(command.subcategoryId()),
                subcategoryName,
                description);

        categoryRepository.save(category);

        return SubcategoryResult.from(subcategory, categoryId.value().toString());
    }
}
