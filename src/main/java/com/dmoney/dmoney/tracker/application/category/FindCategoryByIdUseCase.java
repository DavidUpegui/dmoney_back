package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindCategoryByIdUseCase {
    private final CategoryLoader categoryLoader;

    public CategoryResult execute(String id){
        CategoryId catId = CategoryId.from(id);
        Category category =  categoryLoader.load(catId);
        return CategoryResult.from(category);
    }
}
