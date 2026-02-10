package com.dmoney.dmoney.tracker.application.category.create;

import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.exceptions.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindCategoryByIdUseCase {

    private final CategoryRepository categoryRepository;

    public Category execute(CategoryId id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("id", id.value().toString()));
    }
}
