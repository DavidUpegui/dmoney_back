package com.dmoney.dmoney.tracker.domain.category;


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

    public void rename(SubcategoryName newName){
        this.name = Objects.requireNonNull(newName);
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void edit(SubcategoryName name, String description){
        if(name != null){
            this.name = name;
        }
        if(this.description !=null){
            this.description = description;
        }
    }

    public SubcategoryName name(){
        return name;
    }

    public SubcategoryId id(){
        return id;
    }

    public String description(){
        return description;
    }
}
