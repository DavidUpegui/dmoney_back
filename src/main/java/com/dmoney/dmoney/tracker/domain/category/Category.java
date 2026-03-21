package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Category {
    private final CategoryId id;
    private CategoryName name;
    private Description description;
    private final Set<Subcategory> subcategories = new HashSet<>();

    public Category(
            CategoryId id,
            CategoryName name,
            Description description
    ){
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
    }

    public Category(
            CategoryId id,
            CategoryName name,
            Description description,
            Set<Subcategory> subcategories
    ) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.subcategories.addAll(
                Objects.requireNonNull(subcategories)
        );
    }

    public void edit(CategoryName name, Description description){
        if(name != null){
            this.name = name;
        }
        if(description != null){
            this.description = description;
        }
    }

    public Subcategory addSubcategory(SubcategoryName name, Description description){
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


    public Subcategory editSubcategory(SubcategoryId id, SubcategoryName newName, Description newDescription){
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
    public CategoryName name(){
        return this.name;
    }
    public Description description(){
        return this.description;
    }
    public Set<Subcategory> subcategories(){
        return Collections.unmodifiableSet(this.subcategories);
    }
}
