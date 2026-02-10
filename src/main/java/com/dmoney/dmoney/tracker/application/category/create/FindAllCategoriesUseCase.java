package com.dmoney.dmoney.tracker.application.category.create;


import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllCategoriesUseCase {

    private final CategoryRepository categoryRepository;

    public List<Category> execute(){
        return this.categoryRepository.findAll();
    }

}
