package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.commands.AddSubcategoryCommand;
import com.dmoney.dmoney.tracker.domain.category.*;
import com.dmoney.dmoney.tracker.exceptions.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddSubcategoryUseCase {

    private final CategoryRepository categoryRepository;

    public Subcategory execute(AddSubcategoryCommand command) {
        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "id",
                        command.categoryId().value().toString()
                ));


        Subcategory subcategoryAdded = category.addSubcategory(
                command.name(),
                command.description()
        );
        categoryRepository.save(category);
        return subcategoryAdded;
    }
}
