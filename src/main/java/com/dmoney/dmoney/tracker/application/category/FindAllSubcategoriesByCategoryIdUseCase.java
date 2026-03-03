package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
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

    private final CategoryLoader categoryLoader;


    public List<SubcategoryResult> execute(String categoryId){
        CategoryId catId = CategoryId.from(categoryId);
        Category category = categoryLoader.load(catId);

        return category.subcategories()
                .stream()
                .map(sc -> SubcategoryResult.from(sc, catId.value().toString()))
                .toList();
    }
}
