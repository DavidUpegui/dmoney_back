package com.dmoney.dmoney.tracker.application.category.create;

import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.exceptions.CategoryAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final CategoryRepository repository;

    public Category execute(CreateCategoryCommand command) {
        CategoryName name = new CategoryName(command.name());

        if (repository.existsByName(name)) {
            throw new CategoryAlreadyExistsException("name", name.value());
        }

        Category category = new Category(
                CategoryId.newId(),
                name,
                command.description()
        );
        return repository.save(category);
    }
}
