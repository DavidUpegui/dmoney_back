package com.dmoney.dmoney.tracker.exceptions;

public class CategoryAlreadyExistsException extends ResourceAlreadyExistsException{
    public CategoryAlreadyExistsException(String field, String value){
        super("Category", field, value);
    }
}
