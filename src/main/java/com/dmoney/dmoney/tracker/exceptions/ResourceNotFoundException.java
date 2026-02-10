package com.dmoney.dmoney.tracker.exceptions;

public class ResourceNotFoundException extends DomainException{

    public ResourceNotFoundException(String resource, String field, String value){
        super("The " + resource + " with " + field + "= '" + value + "' was not found");
    }
}
