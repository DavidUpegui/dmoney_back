package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.category.Subcategory;
import com.dmoney.dmoney.tracker.exceptions.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FindAllSubcategoriesByCategoryIdUseCase {

    private final CategoryRepository categoryRepository;


    public Set<Subcategory> execute(CategoryId categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("id", categoryId.value().toString()));

        return category.subcategories();
    }
}
