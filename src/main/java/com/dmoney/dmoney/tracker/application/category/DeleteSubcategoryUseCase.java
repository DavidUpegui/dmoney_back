package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.commands.DeleteSubcategoryCommand;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.category.SubcategoryId;
import com.dmoney.dmoney.tracker.exceptions.SubcategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteSubcategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryLoader categoryLoader;

    public void execute(DeleteSubcategoryCommand command){
        CategoryId categoryId = new CategoryId(UUID.fromString(command.categoryId()));
        Category category = categoryLoader.load(categoryId);

        category.deleteSubcategory(new SubcategoryId(UUID.fromString(command.subcategoryId())));

        categoryRepository.save(category);
    }
}
