package com.dmoney.dmoney.tracker.exceptions;

public class SubcategoryNotFoundException extends ResourceNotFoundException{
    public SubcategoryNotFoundException(String field, String value){
        super("Subcategory", field, value);
    }
}
