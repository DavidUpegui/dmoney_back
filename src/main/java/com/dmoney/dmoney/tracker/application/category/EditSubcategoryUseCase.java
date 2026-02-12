package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.commands.EditSubcategoryCommand;
import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.category.Subcategory;
import com.dmoney.dmoney.tracker.exceptions.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditSubcategoryUseCase {

    private final CategoryRepository categoryRepository;

    public Subcategory execute(EditSubcategoryCommand command){
        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "id",
                        command.categoryId().value().toString()
                ));



        Subcategory subcategory = category.editSubcategory(
                command.subcategoryId(),
                command.subcategoryName(),
                command.subcategoryDescription()
        );

        categoryRepository.save(category);

        return subcategory;
    }
}
