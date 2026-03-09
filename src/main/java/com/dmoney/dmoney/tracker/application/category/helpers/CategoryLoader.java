package com.dmoney.dmoney.tracker.application.category.helpers;

import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component

public class CategoryLoader {

    private final CategoryRepository categoryRepository;

    public CategoryLoader(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public  Category load(CategoryId id){
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Category",
                        "id",
                        id.value().toString()
                ));
    }
}
