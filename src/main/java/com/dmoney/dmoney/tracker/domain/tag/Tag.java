package com.dmoney.dmoney.tracker.domain.tag;

public class Tag {
    TagId id;
    TagName name;
    TagDescription description;

    private Tag( TagId id, TagName name, TagDescription description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static Tag from(TagId id, TagName name, TagDescription description){
        return new Tag(id, name, description);
    }

    public static Tag create(TagName name, TagDescription description){
        return new Tag(TagId.newId(), name, description);
    }

    public TagId id(){
        return this.id;
    }

    public TagName name(){
        return this.name;
    }

    public TagDescription description(){
        return this.description;
    }
}
