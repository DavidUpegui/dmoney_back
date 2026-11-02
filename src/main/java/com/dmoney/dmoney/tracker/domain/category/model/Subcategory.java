package com.dmoney.dmoney.tracker.domain.category.model;


import lombok.ToString;

import java.util.Objects;

@ToString
public class Subcategory {
    private final SubcategoryId id;
    private SubcategoryName name;
    private SubcategoryDescription description;
    private CategoryIcon icon;
    private CategoryColor color;

    public Subcategory(SubcategoryId id, SubcategoryName name, SubcategoryDescription description){
        this(id, name, description, null, null);
    }

    public Subcategory(
            SubcategoryId id,
            SubcategoryName name,
            SubcategoryDescription description,
            CategoryIcon icon,
            CategoryColor color
    ){
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.icon = icon;
        this.color = color;
    }

    public void rename(SubcategoryName newName){
        this.name = Objects.requireNonNull(newName);
    }

    public void changeDescription(SubcategoryDescription description) {
        this.description = description;
    }

    public void changeIcon(CategoryIcon icon) {
        this.icon = icon;
    }

    public void changeColor(CategoryColor color) {
        this.color = color;
    }

    public SubcategoryName name(){
        return name;
    }

    public SubcategoryId id(){
        return id;
    }

    public SubcategoryDescription description(){
        return description;
    }

    public CategoryIcon icon(){
        return icon;
    }

    public CategoryColor color(){
        return color;
    }
}
