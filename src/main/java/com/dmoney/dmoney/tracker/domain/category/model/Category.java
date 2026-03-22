package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Category {
    private final UserId userId;
    private final CategoryId id;
    private CategoryName name;
    private CategoryDescription description;
    private final Set<Subcategory> subcategories = new HashSet<>();

    private Category(
            UserId userId,
            CategoryId id,
            CategoryName name,
            CategoryDescription description
    ){
        this.userId = Objects.requireNonNull(userId);
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
    }

    private Category(
            UserId userId,
            CategoryId id,
            CategoryName name,
            CategoryDescription description,
            Set<Subcategory> subcategories
    ) {
        this.userId = Objects.requireNonNull(userId);
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.subcategories.addAll(
                Objects.requireNonNull(subcategories)
        );
    }

    public static Category create(UserId userId, CategoryName name, CategoryDescription description){
        return new Category(
                userId,
                CategoryId.newId(),
                name,
                description
        );
    }

    public static Category rehydrate(UserId userId,
                              CategoryId categoryId,
                              CategoryName name,
                              CategoryDescription description,
                              Set<Subcategory> subcategories){
        return new Category(
                userId,
                categoryId,
                name,
                description,
                subcategories
        );
    }

    public void edit(CategoryName name, CategoryDescription description){
        if(name != null){
            this.name = name;
        }
        if(description != null){
            this.description = description;
        }
    }

    public Subcategory addSubcategory(SubcategoryName name, SubcategoryDescription description){
        Objects.requireNonNull(name);
        Objects.requireNonNull(description);

        if(hasSubcategoryWithName(name)){
            throw new ResourceAlreadyExistsException("Subcategory", "name", name().value());
        }
        Subcategory subcategory = new Subcategory(
                SubcategoryId.newId(),
                name,
                description
        );
        subcategories.add(subcategory);

        return subcategory;
    }


    public Subcategory editSubcategory(SubcategoryId id, SubcategoryName newName, SubcategoryDescription newDescription){
        Objects.requireNonNull(id, "subcategory ID cannot be null in the edition");
        Subcategory subcategory = findSubcategory(id);

        if (newName != null) {
            if(subcategories.stream()
                    .anyMatch(sc -> !sc.id().equals(id)
                            && sc.name().equals(newName))){
                throw new ResourceAlreadyExistsException("Resource", "name", newName.value());
            }
            subcategory.rename(newName);
        }

        if (newDescription != null) {
            subcategory.changeDescription(newDescription);
        }
        return subcategory;
    }

    public void deleteSubcategory(SubcategoryId subcategoryId){
        boolean removed = subcategories.removeIf(
                sc -> sc.id().equals(subcategoryId)
        );

        if(!removed){
            throw new ResourceNotFoundException("Subcategory", "id", subcategoryId.value().toString());
        }
    }

    private Subcategory findSubcategory(SubcategoryId id) {
        return subcategories.stream()
                .filter(sc -> sc.id().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subcategory", "id", id.value().toString())
                );
    }


    private boolean hasSubcategoryWithName(SubcategoryName name) {
        return subcategories.stream()
                .anyMatch(sc ->
                        sc.name().value().equalsIgnoreCase(name.value()));
    }

    public CategoryId id(){
        return this.id;
    }
    public UserId userId(){
        return this.userId;
    }
    public CategoryName name(){
        return this.name;
    }
    public CategoryDescription description(){
        return this.description;
    }
    public Set<Subcategory> subcategories(){
        return Collections.unmodifiableSet(this.subcategories);
    }
}
