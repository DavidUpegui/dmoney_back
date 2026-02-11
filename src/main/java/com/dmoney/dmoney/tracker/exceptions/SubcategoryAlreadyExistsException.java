package com.dmoney.dmoney.tracker.exceptions;

public class SubcategoryAlreadyExistsException extends ResourceAlreadyExistsException{
    public SubcategoryAlreadyExistsException(String field, String value){
        super("Subcategory", field, value);
    }
}
