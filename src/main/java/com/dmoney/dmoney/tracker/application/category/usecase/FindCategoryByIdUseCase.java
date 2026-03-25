package com.dmoney.dmoney.tracker.application.category.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindCategoryByIdUseCase {
    private final CategoryLoader categoryLoader;
    private final AuthenticatedUserProvider authProvider;

    public CategoryResult execute(String id){
        UserId userId = authProvider.currentUserId();
        CategoryId catId = CategoryId.from(id);
        Category category =  categoryLoader.load(userId, catId);
        return CategoryResult.from(category);
    }
}
