package com.dmoney.dmoney.tracker.domain.tag;

import com.dmoney.dmoney.shared.domain.models.UserId;

public class Tag {
    private final UserId userId;
    private final TagId id;
    private TagName name;
    private TagDescription description;

    private Tag( UserId userId, TagId id, TagName name, TagDescription description){
        this.userId = userId;
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static Tag rehydrate(UserId userId, TagId id, TagName name, TagDescription description){
        return new Tag(userId, id, name, description);
    }

    public static Tag create(UserId userId, TagName name, TagDescription description){
        return new Tag(userId, TagId.newId(), name, description);
    }

    public void changeName(TagName changedName) {
        this.name = changedName;
    }

    public void changeDescription(TagDescription changedDescription) {
        this.description = changedDescription;
    }

    public UserId userId(){return this.userId;}

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
