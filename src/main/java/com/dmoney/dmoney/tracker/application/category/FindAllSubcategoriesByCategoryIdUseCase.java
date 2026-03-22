package com.dmoney.dmoney.tracker.application.category;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.helpers.CategoryLoader;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FindAllSubcategoriesByCategoryIdUseCase {

    private final CategoryLoader categoryLoader;
    private final AuthenticatedUserProvider authProvider;


    public List<SubcategoryResult> execute(String categoryId){
        UserId userId = authProvider.currentUserId();
        CategoryId catId = CategoryId.from(categoryId);
        Category category = categoryLoader.load(userId, catId);

        return category.subcategories()
                .stream()
                .map(sc -> SubcategoryResult.from(sc, catId.value().toString()))
                .toList();
    }
}
