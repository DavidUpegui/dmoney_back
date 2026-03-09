package com.dmoney.dmoney.tracker.application.category;


import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllCategoriesUseCase {

    private final CategoryRepository categoryRepository;

    public List<CategoryResult> execute(){

        return this.categoryRepository.findAll()
                .stream()
                .map(CategoryResult::from)
                .toList();

    }
}
