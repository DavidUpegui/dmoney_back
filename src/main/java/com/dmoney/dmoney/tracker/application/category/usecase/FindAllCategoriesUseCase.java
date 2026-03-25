package com.dmoney.dmoney.tracker.application.category.usecase;


import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllCategoriesUseCase {

    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserProvider authProvider;

    public List<CategoryResult> execute(){
        UserId userId = authProvider.currentUserId();

        return this.categoryRepository.findAllByUserId(userId)
                .stream()
                .map(CategoryResult::from)
                .toList();

    }
}
