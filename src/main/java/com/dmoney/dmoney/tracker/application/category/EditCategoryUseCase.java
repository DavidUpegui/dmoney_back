package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.commands.EditCategoryCommand;
import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.exceptions.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public Category execute(EditCategoryCommand command){

        Category category = categoryRepository.findById(command.id())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "id",
                        command.id().value().toString()
                ));

        category.edit(command.name(), command.description());

        return categoryRepository.save(category);
    }
}
