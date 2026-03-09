package com.dmoney.dmoney.tracker.domain.category;


import lombok.ToString;

import java.util.Objects;

@ToString
public class Subcategory {
    private final SubcategoryId id;
    private SubcategoryName name;
    private Description description;

    public Subcategory(SubcategoryId id, SubcategoryName name, Description description){
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = description;
    }

    public void rename(SubcategoryName newName){
        this.name = Objects.requireNonNull(newName);
    }

    public void changeDescription(Description description) {
        this.description = description;
    }

    public SubcategoryName name(){
        return name;
    }

    public SubcategoryId id(){
        return id;
    }

    public Description description(){
        return description;
    }
}
