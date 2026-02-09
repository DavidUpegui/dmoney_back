package com.dmoney.dmoney.tracker.domain;


import java.util.Objects;

public class Subcategory {
    private final SubcategoryId id;
    private SubcategoryName name;
    private String description;

    public Subcategory(SubcategoryId id, SubcategoryName name, String description){
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = description;
    }

    SubcategoryName name(){
        return name;
    }

    SubcategoryId id(){
        return id;
    }

    public void rename(SubcategoryName newName){
        this.name = Objects.requireNonNull(newName);
    }

    public void changeDescription(String description) {
        this.description = description;
    }
}
