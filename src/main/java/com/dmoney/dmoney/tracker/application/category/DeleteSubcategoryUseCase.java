package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.commands.DeleteSubcategoryCommand;
import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.exceptions.SubcategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSubcategoryUseCase {

    private final CategoryRepository categoryRepository;

    public void execute(DeleteSubcategoryCommand command){
        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(
                        () -> new SubcategoryNotFoundException(
                                "Id", command.categoryId().value().toString()
                        )
                );

        category.deleteSubcategory(command.subcategoryId());

        categoryRepository.save(category);
    }
}
