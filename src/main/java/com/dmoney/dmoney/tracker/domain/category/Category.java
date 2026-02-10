package com.dmoney.dmoney.tracker.domain.category;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Category {
    private final CategoryId id;
    private CategoryName name;
    private String description;
    private final Set<Subcategory> subcategories = new HashSet<>();

    public Category(
            CategoryId id,
            CategoryName name,
            String description
    ){
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
    }

    public Category(
            CategoryId id,
            CategoryName name,
            String description,
            Set<Subcategory> subcategories
    ) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.subcategories.addAll(
                Objects.requireNonNull(subcategories)
        );
    }

    public void addSubcategory(Subcategory subcategory){
        Objects.requireNonNull(subcategory);

        if(hasSubcategoryWithName(subcategory.name())){
            throw new IllegalArgumentException(
                    "Subcategory with same name already exists in this category"
            );

        }
        subcategories.add(subcategory);
    }


    public void renameSubcategory(SubcategoryId id, SubcategoryName newName){
        if(hasSubcategoryWithName(newName)){
            throw new IllegalArgumentException("Duplicate subcategory name");
        }

        Subcategory sc = findSubcategory(id);
        sc.rename(newName);
    }
    private Subcategory findSubcategory(SubcategoryId id) {
        return subcategories.stream()
                .filter(sc -> sc.id().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Subcategory not found in this category")
                );
    }


    private boolean hasSubcategoryWithName(SubcategoryName name) {
        return subcategories.stream()
                .anyMatch(sc -> sc.name().equals(name));
    }

    public CategoryId id(){
        return this.id;
    }
    public CategoryName name(){
        return this.name;
    }
    public String description(){
        return this.description;
    }
    public Set<Subcategory> subcategories(){
        return this.subcategories;
    }
}
