package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.category.Subcategory;
import com.dmoney.dmoney.tracker.exceptions.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FindAllSubcategoriesByCategoryIdUseCase {

    private final CategoryRepository categoryRepository;


    public List<SubcategoryResult> execute(String categoryId){
        CategoryId catId = CategoryId.from(categoryId);
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("id", catId.value().toString()));

        return category.subcategories()
                .stream()
                .map(sc -> SubcategoryResult.from(sc, catId.value().toString()))
                .toList();
    }
}
