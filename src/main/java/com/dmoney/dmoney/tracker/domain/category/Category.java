package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.tracker.exceptions.SubcategoryAlreadyExistsException;
import com.dmoney.dmoney.tracker.exceptions.SubcategoryNotFoundException;

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

    public void edit(CategoryName name, String description){
        if(name != null){
            this.name = name;
        }
        if(description != null){
            this.description = description;
        }
    }

    public Subcategory addSubcategory(SubcategoryName name, String description){
        Objects.requireNonNull(name);

        if(hasSubcategoryWithName(name)){
            throw new SubcategoryAlreadyExistsException("name", name().value());

        }

        Subcategory subcategory = new Subcategory(
                SubcategoryId.newId(),
                name,
                description
        );
        subcategories.add(subcategory);

        return subcategory;
    }


    public Subcategory editSubcategory(SubcategoryId id, SubcategoryName name, String description){
        Subcategory subcategory = subcategories.stream()
                .filter(sc -> sc.id().equals(id))
                .findFirst()
                .orElseThrow( () ->
                        new SubcategoryNotFoundException("id", id.value().toString()));

        if(name != null && subcategories.stream()
                .anyMatch(sc -> !sc.id().equals(id)
                && sc.name().equals(name))){
            throw new SubcategoryAlreadyExistsException("name", name().value());
        }

        subcategory.edit(name, description);
        return subcategory;
    }

    private Subcategory findSubcategory(SubcategoryId id) {
        return subcategories.stream()
                .filter(sc -> sc.id().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new SubcategoryNotFoundException("id", id.value().toString())
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
