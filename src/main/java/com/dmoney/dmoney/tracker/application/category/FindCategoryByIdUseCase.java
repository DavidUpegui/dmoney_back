package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
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

    public CategoryResult execute(String id){
        CategoryId catId = CategoryId.from(id);
        Category category =  categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("id", catId.value().toString()));
        return CategoryResult.from(category);
    }
}
