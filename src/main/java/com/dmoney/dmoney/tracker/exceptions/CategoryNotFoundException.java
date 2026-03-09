package com.dmoney.dmoney.tracker.exceptions;

public class CategoryNotFoundException extends ResourceNotFoundException{
    public CategoryNotFoundException(String field, String value){
        super("Category", field, value);
    }
}
