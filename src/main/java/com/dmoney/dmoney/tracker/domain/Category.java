package com.dmoney.dmoney.tracker.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Category {
    private final CategoryId id;
    private CategoryName name;
    private String description;
    private final Set<Subcategory> subcategoryList = new HashSet<>();

    public Category(
            CategoryId id,
            CategoryName name,
            String description
    ){
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
    }

    public void addSubcategory(Subcategory subcategory){
        Objects.requireNonNull(subcategory);

        if(hasSubcategoryWithName(subcategory.name())){
            throw new IllegalArgumentException(
                    "Subcategory with same name already exists in this category"
            );

        }
        subcategoryList.add(subcategory);
    }

    public void renameSubcategory(SubcategoryId id, SubcategoryName newName){
        if(hasSubcategoryWithName(newName)){
            throw new IllegalArgumentException("Duplicate subcategory name");
        }

        Subcategory sc = findSubcategory(id);
        sc.rename(newName);
    }
    private Subcategory findSubcategory(SubcategoryId id) {
        return subcategoryList.stream()
                .filter(sc -> sc.id().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Subcategory not found in this category")
                );
    }


    private boolean hasSubcategoryWithName(SubcategoryName name) {
        return subcategoryList.stream()
                .anyMatch(sc -> sc.name().equals(name));
    }
}
