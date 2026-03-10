package com.dmoney.dmoney.tracker.domain.tag;

public record TagName(
        String value
) {
    public TagName(String value){
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException("Tag name cannot be null");
        }
        this.value = value.trim();
    }

    public static TagName from(String name){
        return new TagName(name);
    }
}
