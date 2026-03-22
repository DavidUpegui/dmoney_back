package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import org.springframework.stereotype.Service;

@Service
public class CategoryUniquenessChecker {
    private final CategoryRepository categoryRepository;

    public CategoryUniquenessChecker(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public void check(UserId userId, CategoryName name){
        if(categoryRepository.existsByUserIdAndNameIgnoreCase(userId, name)){
            throw new ResourceAlreadyExistsException("Category", "name", name.value());
        }
    }

    public void check(UserId userId, CategoryName newName, CategoryName currentName){
        if(!newName.value().equalsIgnoreCase(currentName.value())){
            check(userId, newName);
        }
    }
}
